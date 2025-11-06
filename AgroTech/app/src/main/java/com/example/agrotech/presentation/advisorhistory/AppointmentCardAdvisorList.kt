package com.example.agrotech.presentation.advisorhistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agrotech.domain.appointment.Appointment

@Composable
fun AppointmentCardAdvisorList(
    appointments: List<Appointment>,
    farmerNames: Map<Long, String>,
    farmerImagesUrl: Map<Long, String>,
    onAppointmentClick: (Appointment) -> Unit // Pass a lambda for navigation
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            appointments.forEachIndexed { index, appointment ->
                val farmerName = farmerNames[appointment.id] ?: "Nombre no disponible"
                val farmerImageUrl = farmerImagesUrl[appointment.id] ?: ""

                AppointmentCardAdvisor(
                    appointment = appointment,
                    farmerName = farmerName,
                    farmerImageUrl = farmerImageUrl,
                    onClick = { onAppointmentClick(appointment) } // Pass the click action
                )

                if (index < appointments.size - 1) {
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        color = Color.Black.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}
