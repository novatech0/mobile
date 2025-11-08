package com.example.agrotech.presentation.createprofilefarmer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.agrotech.R
import java.io.File

@Composable
fun CreateProfileFarmerScreen(viewModel: CreateProfileFarmerViewModel) {
    val state by viewModel.state
    val snackbarMessage by viewModel.snackbarMessage
    val snackbarHostState = remember { SnackbarHostState() }
    val photo by viewModel.photo

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
            viewModel.onPhotoChanged(file)
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.15f)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.starheader),
                        contentDescription = "Header star Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Text(
                    text = "Crear Perfil",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(50.dp))

                AsyncImage(
                    model = photo,
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.profile_icon),
                    error = painterResource(R.drawable.profile_icon)
                )


                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .clickable {
                            imagePicker.launch("image/*")
                        }
                        .background(Color(0xFF3E64FF), shape = MaterialTheme.shapes.medium)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Subir foto de perfil",
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp) // Alinear a la izquierda
                )

                // TextField for Description
                TextField(
                    value = viewModel.description.value,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    placeholder = { Text("Cuéntanos un poco sobre ti") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(80.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            viewModel.createProfile()
                        }
                        .background(Color(0xFF092C4C), shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continuar",
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}