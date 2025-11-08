package com.example.agrotech.presentation.farmerappointmentdetail

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import com.example.agrotech.presentation.farmerhistory.AppointmentCard

@Composable
fun FarmerAppointmentDetailScreen(
    viewModel: FarmerAppointmentDetailViewModel,
    appointmentId: Long,
) {
    val appointment = viewModel.appointmentDetails.observeAsState().value
    val isLoading = viewModel.isLoading.observeAsState(false).value
    val errorMessage = viewModel.errorMessage.observeAsState().value
    val showCancelDialog = viewModel.showCancelDialog.observeAsState(false).value
    val clipboardManager: Clipboard = LocalClipboard.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.startPollingAppointmentStatus(appointmentId)
        viewModel.loadAppointmentDetails(appointmentId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopPollingAppointmentStatus()
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Parte superior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { viewModel.goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Text(
                        text = "Detalles de la Cita",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { /* Acción de más opciones */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones"
                        )
                    }
                }

                // Contenido principal
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator()
                        }
                        errorMessage != null -> {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        appointment != null -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Card para mostrar la información del asesor y la cita
                                AppointmentCard(
                                    appointment = appointment,
                                    onClick = null
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Link de la videoconferencia
                                Text(
                                    text = "Link de la videoconferencia",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                OutlinedTextField(
                                    value = appointment.meetingUrl,
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            copyToClipboard(
                                                context = context,
                                                clipboardManager = clipboardManager,
                                                text = appointment.meetingUrl
                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copiar enlace"
                                            )
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Comentario brindado por el usuario
                                Text(
                                    text = "Comentario brindado por ti",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                OutlinedTextField(
                                    value = appointment.message,
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    readOnly = true
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                // Botón para cancelar la cita
                                Button(
                                    onClick = { viewModel.onCancelAppointmentClick() },
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
                        else -> {
                            Text(
                                text = "No se encontró la información de la cita.",
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            // Mostrar diálogo de cancelación
            if (showCancelDialog) {
                CancelAppointmentDialog(
                    onDismiss = { viewModel.onDismissCancelDialog() },
                    onConfirm = { reason -> viewModel.cancelAppointment(appointmentId, reason) }
                )
            }
        }
    }
}

fun copyToClipboard(context: Context, clipboardManager: Clipboard, text: String) {
    clipboardManager.nativeClipboard.setPrimaryClip(ClipData.newPlainText("meeting url", text))
    Toast.makeText(context, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show()
}