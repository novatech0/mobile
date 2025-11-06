package com.example.agrotech.presentation.farmerappointmentdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip

@Composable
fun CancelAppointmentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val (cancelReason, setCancelReason) = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón para cerrar el diálogo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            // Texto principal
            Text(
                text = "¿Seguro que deseas cancelar tu cita?",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            // Mensaje adicional
            Text(
                text = "Por favor, ingresa el motivo por el cual cancelarás la cita",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            // Campo de texto para el motivo
            OutlinedTextField(
                value = cancelReason,
                onValueChange = setCancelReason,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Ingresa tu mensaje...") }
            )

            // Botón para confirmar la cancelación
            Button(
                onClick = { onConfirm(cancelReason) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = cancelReason.isNotBlank(),
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
}