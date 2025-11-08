package com.example.agrotech.presentation.advisorpostdetail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.agrotech.R
import java.io.File

@Composable
fun AdvisorPostDetailScreen(viewModel: AdvisorPostDetailViewModel, postId: Long) {
    LaunchedEffect(Unit) {
        viewModel.getPost(postId)
    }

    val state = viewModel.state.value
    val title = viewModel.title.value
    val description = viewModel.description.value
    val isExpanded = viewModel.expanded.value
    val imageFile = viewModel.image.value
    val imageUrl = viewModel.imageUrl.value

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val fileName = uri.lastPathSegment ?: "imagen.jpg"
            val file = File(context.cacheDir, fileName)
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            viewModel.onUpdateImage(file)
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.goBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Publicación",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                IconButton(onClick = { viewModel.setExpanded(true) }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(32.dp)
                    )
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { viewModel.setExpanded(false) },
                    offset = DpOffset(x = (2000).dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Eliminar",
                                style = TextStyle(fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                            )
                        },
                        onClick = {
                            viewModel.deletePost(postId)
                        }
                    )
                }
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.data == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message.ifBlank { "Error al cargar la publicación" } ,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                else -> {
                    AsyncImage(
                        model = imageFile ?: imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.placeholder)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    imagePicker.launch("image/*")
                                }
                                .background(Color(0xFF3E64FF), shape = MaterialTheme.shapes.medium)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Subir foto",
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Text(
                        text = "Título",
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { viewModel.onUpdateTitle(it) },
                        placeholder = { Text("Título de la publicación") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Descripción",
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.onUpdateDescription(it) },
                        placeholder = { Text("Descripción de la publicación") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updatePost(postId)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
