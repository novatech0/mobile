package com.example.agrotech.presentation.advisorappointments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.agrotech.presentation.advisorhistory.AppointmentCardAdvisorList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Routes


@Composable
fun AdvisorAppointmentsScreen(
    navController: NavController,
    viewModel: AdvisorAppointmentsViewModel
) {
    val appointments = viewModel.appointments
    val availableDates = viewModel.availableDates
    val farmerNames = viewModel.farmerNames
    val farmerImagesUrl = viewModel.farmerImagesUrl
    val isExpanded = viewModel.expanded.value


    LaunchedEffect(GlobalVariables.USER_ID, GlobalVariables.TOKEN) {
        if (GlobalVariables.USER_ID != 0L && GlobalVariables.TOKEN.isNotBlank()) {
            viewModel.loadAppointmentsAgain() // deberías exponer una función pública en el VM para volver a cargar
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
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
                    text = "Citas",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            IconButton(onClick = { navController.navigate(Routes.AppointmentsAdvisorHistoryList.route) }) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    modifier = Modifier
                        .size(32.dp)
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


        AppointmentCardAdvisorList(
            appointments = appointments,
            availableDates = availableDates,
            farmerNames = farmerNames,
            farmerImagesUrl = farmerImagesUrl,
            onAppointmentClick = { appointment ->
                navController.navigate(Routes.AdvisorAppointmentDetail.route + "/${appointment.id}")
            }
        )
    }
}
