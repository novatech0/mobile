package com.example.agrotech.presentation.advisorhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrotech.R
import com.example.agrotech.common.Routes
import com.example.agrotech.presentation.advisorhistory.AppointmentCardAdvisorList
import com.example.agrotech.presentation.navigationcard.CardItem
import com.example.agrotech.presentation.navigationcard.NavigationCard


@Composable
fun AdvisorHomeScreen(
    navController: NavController,
    viewModel: AdvisorHomeViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
        viewModel.getNotificationCount()
    }

    val cardItems = listOf(
        CardItem(
            image = painterResource(id = R.drawable.icon_publications_advisor),
            text = "Mis Publicaciones",
            onClick = { navController.navigate(Routes.AdvisorPosts.route) }
        ),
        CardItem(
            image = painterResource(id = R.drawable.icon_appointments),
            text = "Citas",
            onClick = { navController.navigate(Routes.AppointmentsAdvisorList.route) }
        ),
        CardItem(
            image = painterResource(id = R.drawable.icon_available_date),
            text = "Mis horarios",
            onClick = { navController.navigate(Routes.AdvisorAvailableDates.route) }
        )
    )

    val appointments = viewModel.appointments
    val availableDates = viewModel.availableDates
    val advisorId = viewModel.advisorId
    val advisorName = viewModel.advisorName
    val farmerNames = viewModel.farmerNames
    val farmerImagesUrl = viewModel.farmerImagesUrl
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val isExpanded = viewModel.expanded.value

    val upcomingAppointments = appointments
        .filter { it.status == "PENDING" || it.status == "ONGOING" }
        .sortedBy { appointment ->
            val availableDate = viewModel.availableDates.find { it.id == appointment.availableDateId }
            availableDate?.scheduledDate
        }
        .take(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bienvenido(a), ${advisorName ?: "Loading..."}",
                modifier = Modifier.padding(vertical = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(Routes.NotificationList.route) }) {
                BadgedBox(
                    badge = {
                        if (viewModel.notificationCount.value > 0) {
                            Badge(
                                contentColor = Color.White,
                                containerColor = Color.Red,
                                modifier = Modifier.offset(x = (-8).dp, y = (0).dp)
                            ) {
                                Text(
                                    text = viewModel.notificationCount.value.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(32.dp)
                    )
                }
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

        when {
            isLoading -> {
                LoadingView()
                return@Column
            }
            errorMessage != null -> {
                ErrorView(message = errorMessage)
                return@Column
            }
        }

        Image(
            painter = painterResource(id = R.drawable.hero_image),
            contentDescription = "Hero Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .padding(bottom = 16.dp)
        )
        Text(
            text = "Tu próxima cita",
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        if (advisorId != null && upcomingAppointments.isNotEmpty()) {
            AppointmentCardAdvisorList(
                appointments = upcomingAppointments,
                availableDates = availableDates,
                farmerNames = farmerNames,
                farmerImagesUrl = farmerImagesUrl,
                onAppointmentClick = { appointment ->
                    navController.navigate(Routes.AdvisorAppointmentDetail.route + "/${appointment.id}")
                }
            )
        } else {
            Text(
                text = "No tienes citas programadas",
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = "Elige tu próximo paso",
            modifier = Modifier.padding(top = 40.dp, bottom = 16.dp),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(count = cardItems.size) { index ->
                NavigationCard(index, cardItems)
            }
        }
    }


}


@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Loading your appointments...")
    }
}

@Composable
fun ErrorView(message: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: ${message ?: "Unknown error"}", color = MaterialTheme.colorScheme.error)
    }
}
