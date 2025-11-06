package com.example.agrotech.presentation.advisorhistory


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.presentation.advisorappointments.AdvisorAppointmentsViewModel


@Composable
fun AdvisorAppointmentsHistoryScreen(
    viewModel: AdvisorAppointmentsViewModel
) {
    val appointments = viewModel.appointments
    val farmerNames = viewModel.farmerNames
    val farmerImagesUrl = viewModel.farmerImagesUrl
    val isExpanded = viewModel.expanded.value


    LaunchedEffect(GlobalVariables.USER_ID, GlobalVariables.TOKEN) {
        if (GlobalVariables.USER_ID != 0L && GlobalVariables.TOKEN.isNotBlank()) {
            viewModel.loadAppointmentsCompletedAgain()
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
                    text = "Historial",
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
                        viewModel.goToProfile()
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
                    }
                )
            }
        }


        AppointmentCardAdvisorList(
            appointments = appointments,
            farmerNames = farmerNames,
            farmerImagesUrl = farmerImagesUrl,
            onAppointmentClick = { appointment ->
                viewModel.goToAppointmentDetails(appointment.id)
            }
        )
    }
}
