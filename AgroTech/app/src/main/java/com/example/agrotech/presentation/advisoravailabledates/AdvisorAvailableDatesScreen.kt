package com.example.agrotech.presentation.advisoravailabledates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrotech.common.Routes
import com.example.agrotech.domain.appointment.AvailableDate
import com.example.agrotech.domain.appointment.CreateAvailableDate

@Composable
fun AdvisorAvailableDatesScreen(
    navController: NavController,
    viewModel: AdvisorAvailableDatesViewModel
) {
    val availableDates by viewModel.availableDates.observeAsState(emptyList())
    val isExpanded = viewModel.expanded.value


    var showDialog by remember { mutableStateOf(false) }
    var availableDateInput by remember { mutableStateOf(TextFieldValue("")) }
    var startTimeInput by remember { mutableStateOf(TextFieldValue("")) }
    var endTimeInput by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.initScreen()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF092C4C),
                contentColor = Color.White
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
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
                        text = "Mis Horarios Disponibles",
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
                            navController.navigate(Routes.AdvisorProfile.route)
                            viewModel.setExpanded(false)
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableDates ?: emptyList()) { date ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "📅 Fecha: ${date.scheduledDate}")
                                Text(text = "🕒 Desde: ${date.startTime}")
                                Text(text = "🕓 Hasta: ${date.endTime}")
                            }
                            IconButton(
                                onClick = { viewModel.deleteAvailableDate(date.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Crear Horario Disponible") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = availableDateInput,
                            onValueChange = { availableDateInput = it },
                            label = { Text("Fecha disponible (YYYY-MM-DD)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        OutlinedTextField(
                            value = startTimeInput,
                            onValueChange = { startTimeInput = it },
                            label = { Text("Hora de inicio (HH:mm)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )

                        OutlinedTextField(
                            value = endTimeInput,
                            onValueChange = { endTimeInput = it },
                            label = { Text("Hora de fin (HH:mm)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )

                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val advisorId = viewModel.advisorId
                            if (advisorId != null) {
                                val newDate = CreateAvailableDate(
                                    advisorId = advisorId,
                                    scheduledDate = availableDateInput.text,
                                    startTime = startTimeInput.text,
                                    endTime = endTimeInput.text,
                                )
                                viewModel.createAvailableDate(newDate)
                                showDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF27AE60), // Verde
                            contentColor = Color.White // Texto blanco
                        )
                    ) {
                        Text("Crear")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF84343), // Rojo
                            contentColor = Color.White // Texto blanco
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
