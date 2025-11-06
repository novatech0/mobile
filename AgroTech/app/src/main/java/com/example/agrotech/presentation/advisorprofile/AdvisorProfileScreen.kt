package com.example.agrotech.presentation.advisorprofile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AdvisorProfileScreen(viewModel: AdvisorProfileViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getAdvisorProfile()
    }

    val state = viewModel.state.value
    val isUploadingImage = viewModel.isUploadingImage.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.goToHome() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
                Text(text = "Editar Perfil", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.size(48.dp)) // layout fix
            }

            if (state.isLoading || isUploadingImage) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                state.data?.let {
                    EditProfileContent(viewModel = viewModel)
                } ?: run {
                    state.message.let { errorMessage ->
                        if (errorMessage.isNotBlank()) {
                            AlertDialog(
                                onDismissRequest = { viewModel.getAdvisorProfile() },
                                confirmButton = {
                                    TextButton(onClick = { viewModel.getAdvisorProfile() }) {
                                        Text("OK")
                                    }
                                },
                                title = { Text("Error") },
                                text = { Text(errorMessage) }
                            )
                        }
                        else {
                            Text(
                                text = "No se encontraron datos",
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}



