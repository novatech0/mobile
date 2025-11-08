package com.example.agrotech.presentation.newappointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentScreen(viewModel: NewAppointmentViewModel, advisorId: Long) {
    val comment = viewModel.comment.value
    val state = viewModel.state.value
    val isExpanded = viewModel.isExpanded.value
    val selectedDate = viewModel.selectedDate.value

    LaunchedEffect(Unit) {
        viewModel.getAvailableDates(advisorId)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(16.dp)
        )    {
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
                        text = "Nueva Cita",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Seleccione el horario y la fecha",
                    color = Color(0xFF222B45),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { viewModel.toggleExpanded() },
                        content = {
                            TextField(
                                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                                value = if (selectedDate == -1) {
                                    "Seleccione una fecha"
                                } else {
                                    state.data?.let { "${it[selectedDate].availableDate} - ${it[selectedDate].startTime} a ${it[selectedDate].endTime}" }?: "Fecha no disponible"
                                },
                                onValueChange = { /* Do nothing */ },
                                readOnly = true
                            )
                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { viewModel.toggleExpanded() }
                            ) {
                                state.data?.forEachIndexed { index, date ->
                                    DropdownMenuItem(
                                        text = { Text(text = "${date.availableDate} - ${date.startTime} a ${date.endTime}") },
                                        onClick = {
                                            viewModel.setSelectedDate(index)
                                            viewModel.toggleExpanded()
                                        }
                                    )
                                }
                            }

                        }
                    )
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Coméntame tu problema",
                    color = Color(0xFF222B45),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = comment,
                    onValueChange = { viewModel.setComment(it) },
                    label = { Text("Escribe tu comentario") },
                    modifier = Modifier.fillMaxWidth().height(150.dp).padding(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        onClick = { viewModel.createAppointment(advisorId) },
                        enabled = selectedDate != -1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E64FF), // Color de fondo del botón
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        ) {
                        Text(
                            text = "Confirmar Cita",
                            color = Color(0xFFFFFFFF),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        )
                    }
                }
            }
        }
    }
}

