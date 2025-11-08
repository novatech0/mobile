package com.example.agrotech.presentation.rating

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.agrotech.R

@Composable
fun FarmerReviewAppointmentScreen(
    viewModel: FarmerReviewAppointmentViewModel,
    appointmentId: Long
) {
    LaunchedEffect(Unit) {
        viewModel.loadAdvisorDetails(appointmentId)
    }

    val advisorName = viewModel.advisorName.value
    val advisorImage = viewModel.advisorImage.value
    val comment = viewModel.comment.value?.takeIf { it.isNotBlank() } ?: ""
    val rating = viewModel.rating.value?.takeIf { it in 1..5 } ?: 0
    val isSubmitting = viewModel.isSubmitting.value
    val state = viewModel.state.value
    val hasReview = viewModel.hasReview.value // Detectar si ya hay una reseña existente

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { viewModel.goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }

                if (advisorImage.isNotEmpty()) {
                    AsyncImage(
                        model = advisorImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.placeholder)
                    )
                }

                Text(
                    text = advisorName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Calificación (rating)
                Text(
                    text = "Calificación",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                RatingBar(
                    currentRating = rating,
                    onRatingChange = { viewModel.setRating(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Comentario
                Text(
                    text = "Comentario",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, top = 8.dp)
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { viewModel.setComment(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text(text = "Escribe tu comentario...") },
                    label = { Text(text = "Comentario") },
                    maxLines = 4,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de enviar reseña o actualizar reseña
                Button(
                    onClick = { viewModel.submitReview(appointmentId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSubmitting
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = if (hasReview) "Actualizar reseña" else "Enviar reseña"
                    )
                    Text(text = if (hasReview) "Actualizar Reseña" else "Enviar Reseña")
                }
            }

            state.message?.takeIf { it.isNotEmpty() }?.let { message ->
                Snackbar(
                    action = {
                        TextButton(onClick = { viewModel.clearState() }) {
                            Text("OK")
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = message)
                }
            }

            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun RatingBar(currentRating: Int, onRatingChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center) {
        for (i in 1..5) {
            IconButton(onClick = { onRatingChange(i) }) {
                Icon(
                    imageVector = if (i <= currentRating) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = "Calificación $i",
                    tint = if (i <= currentRating) Color(0xFFE4A70A) else Color.Gray
                )
            }
        }
    }
}