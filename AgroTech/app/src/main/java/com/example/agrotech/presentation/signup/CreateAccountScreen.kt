package com.example.agrotech.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrotech.R
import com.example.agrotech.common.Routes

@Composable
fun CreateAccountScreen(
    navController: NavController,
    viewModel: CreateAccountViewModel) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.15f)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.starheader),
                        contentDescription = "Header star Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Text(
                    text = "Crear Cuenta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Para empezar debe elegir un rol",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left
                )

                // Contenedor para los botones
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center // Centra el contenido del box
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, // Centra los botones horizontalmente
                        verticalArrangement = Arrangement.Center // Centra verticalmente dentro del Box
                    ) {
                        // Botón de Granjero
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp) // Espacio entre botones
                                .clickable {
                                    viewModel.goToFormsFarmer()
                                    navController.navigate(Routes.CreateAccountFarmer.route)
                                }
                                .background(Color(0xFFFF7121), shape = MaterialTheme.shapes.medium) // Color y forma redondeada
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Granjero",
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Text(
                            text = "Si necesita ayuda para gestionar tu granja.",
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        // Botón de Asesor
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.goToFormsAdvisor()
                                    navController.navigate(Routes.CreateAccountAdvisor.route)
                                }
                                .background(Color(0xFF27AE60), shape = MaterialTheme.shapes.medium)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Asesor",
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Text(
                            text = "Si deseas brindar asesoría a granjeros",
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = buildAnnotatedString {
                        append("¿Ya tienes una cuenta? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Inicia sesión")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { navController.navigate(Routes.SignIn.route) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

