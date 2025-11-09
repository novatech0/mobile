package com.example.agrotech.presentation.farmerappointments

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.agrotech.common.Routes
import com.example.agrotech.presentation.farmerhistory.AppointmentCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FarmerAppointmentListScreen(
    navController: NavController,
    viewModel: FarmerAppointmentListViewModel) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fecha seleccionada (por defecto null para mostrar todas las citas)
    val selectedDate = remember { mutableStateOf<Date?>(null) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate.value = calendar.time
            viewModel.getAdvisorAppointmentListByFarmer(selectedDate.value)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Formatear la fecha seleccionada
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val selectedDateText = selectedDate.value?.let { dateFormat.format(it) } ?: "Seleccionar fecha"

    LaunchedEffect(Unit) {
        viewModel.getAdvisorAppointmentListByFarmer(null)
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.navigate(Routes.FarmerHome.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                    Text(
                        text = "Citas",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row {
                        IconButton(onClick = { navController.navigate(Routes.FarmerAppointmentHistory.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.History,
                                contentDescription = "Historial"
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones"
                            )
                        }
                    }
                }

                // Filter Chips for date filtering
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // FilterChip to select a specific date
                    FilterChip(
                        selected = selectedDate.value != null,
                        onClick = { datePickerDialog.show() },
                        label = { Text(selectedDateText) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        },

                    )

                    // FilterChip to show all appointments
                    FilterChip(
                        selected = selectedDate.value == null,
                        onClick = {
                            selectedDate.value = null
                            viewModel.getAdvisorAppointmentListByFarmer(null)
                        },
                        label = { Text("Mostrar todas") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Mostrar todas las citas"
                            )
                        },

                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    state.data?.let { appointments ->
                        items(appointments) { appointment ->
                            AppointmentCard(
                                appointment = appointment,
                                onClick = { navController.navigate(Routes.FarmerAppointmentDetail.route + "/${appointment.id}") }
                            )
                        }
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
                    if (state.data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.message.ifEmpty { "No hay citas programadas" },
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF222B45),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
