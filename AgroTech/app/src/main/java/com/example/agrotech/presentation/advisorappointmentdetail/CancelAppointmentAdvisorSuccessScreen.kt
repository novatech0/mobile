package com.example.agrotech.presentation.advisorappointmentdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController
import com.example.agrotech.common.Routes

@Composable
fun CancelAppointmentAdvisorSuccessScreen(
    navController: NavController,
    viewModel: AdvisorAppointmentDetailViewModel) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Botón de retroceso en la parte superior izquierda
            IconButton(
                onClick = { navController.navigate(Routes.AppointmentsAdvisorList.route) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F3F5)) // Fondo gris claro
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            // Contenido principal centrado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Emoji de confirmación
                Text(
                    text = "\uD83D\uDE4C", // Emoji de manos levantadas
                    fontSize = 64.sp, // Tamaño del emoji más grande
                    modifier = Modifier.padding(bottom = 16.dp) // Espacio debajo del emoji
                )

                // Título de confirmación
                Text(
                    text = "¡Cita cancelada!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtítulo de confirmación
                Text(
                    text = "¡Tu cita ha sido cancelada!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 18.sp, // Ajuste del tamaño del texto
                    lineHeight = 24.sp, // Ajuste de la altura de línea
                    textAlign = TextAlign.Center // Corrected textAlign type
                )
            }
        }
    }
}