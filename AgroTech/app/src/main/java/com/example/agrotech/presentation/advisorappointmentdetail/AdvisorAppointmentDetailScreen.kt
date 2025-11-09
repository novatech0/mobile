package com.example.agrotech.presentation.advisorappointmentdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.agrotech.common.Routes
import kotlinx.coroutines.launch

@Composable
fun AdvisorAppointmentDetailScreen(
    navController: NavController,
    appointmentId: Long,
    viewModel: AdvisorAppointmentDetailViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(appointmentId) {
        viewModel.loadAppointmentDetails(appointmentId)
        viewModel.loadReviewForAppointment(appointmentId)
    }

    val appointment = viewModel.appointmentDetail.value
    val availableDate = viewModel.availableDate.value
    val isExpanded = viewModel.expanded.value
    val review = viewModel.appointmentReviews.value[appointmentId]

    if (appointment != null && availableDate != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
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
                        text = "Detalles de la cita",
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
                                text = "Perfil",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            )
                        },
                        onClick = {
                            viewModel.setExpanded(false)
                            navController.navigate(Routes.AdvisorProfile.route)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Salir",
                                style = TextStyle(fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                            )
                        },
                        onClick = {
                            viewModel.signOut()
                            viewModel.setExpanded(false)
                            navController.navigate(Routes.Welcome.route) { popUpTo(0) }
                        }
                    )
                }
            }
            AdvisorAppointmentDetailCard(
                availableDate = availableDate,
                farmerName = viewModel.farmerProfile.value?.firstName ?: "Nombre no disponible",
                farmerImageUrl = viewModel.farmerProfile.value?.photo ?: ""
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Link de la videoconferencia",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = appointment.meetingUrl,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (appointment.status != "COMPLETED") {
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Comentario del usuario",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = appointment.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (appointment.status == "COMPLETED") {
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Calificación brindada por el usuario",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                RatingBar(
                    currentRating = review?.second ?: 0,
                    onRatingChange = {} // Empty lambda since this is for display only
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Comentario",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = review?.first ?: "No disponible",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (appointment.status != "COMPLETED") {
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.cancelAppointment()
                            navController.navigate(Routes.ConfirmDeletionAppointmentAdvisor.route)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Cancelar Cita",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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