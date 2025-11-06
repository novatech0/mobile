package com.example.agrotech.presentation.confirmcreationaccountadvisor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ConfirmCreationAccountAdvisorScreen(viewModel: ConfirmCreationAccountAdvisorViewModel) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center // Centra todo el contenido
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                // Emoji
                Text(
                    text = "🧑‍💻😁",
                    fontSize = 64.sp // Tamaño del emoji
                )

                // Línea 2
                Text(
                    text = "¡Cuenta creada!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Línea 3
                Text(
                    text = "¡Tu cuenta de asesor ha sido creada correctamente!",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Botón Continuar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            viewModel.goToAdvisorHomeScreen()
                        }
                        .background(Color(0xFF092C4C), shape = RoundedCornerShape(8.dp)) // Esquinas redondeadas
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continuar",
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}