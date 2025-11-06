package com.example.agrotech.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.sharp.Email
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.agrotech.R


@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val email = viewModel.email.value
    val password = viewModel.password.value

    LaunchedEffect(state.message) {
        if (state.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.message)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 64.dp)
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.starheader),
                    contentDescription = "Header star Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.15f),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = "Iniciar sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    ),
                    textAlign = TextAlign.Left
                )

                TextField(
                    value = email,
                    onValueChange = { viewModel.setEmail(it) },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp)),
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Sharp.Email,
                            contentDescription = "Email"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(password, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.signIn() },
                    enabled = email.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF092C4C))
                ) {
                    Text(text = "Iniciar sesión", color = Color.White)
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = buildAnnotatedString {
                        append("¿No tienes cuenta? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Crea tu cuenta")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            viewModel.goToSignUpScreen()
                        },
                    color = Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

        }
    }
}

@Composable
fun PasswordTextField(password: String, viewModel: LoginViewModel) {
    val showPassword = viewModel.isPasswordVisible.value
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    TextField(
        value = password,
        onValueChange = { viewModel.setPassword(it) },
        label = { Text("Contraseña") },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
        trailingIcon = {
            Icon(
                imageVector = if (showPassword) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                },
                contentDescription = "Toggle password visibility",
                modifier = Modifier
                    .clickable { viewModel.togglePasswordVisibility() }
            )
        }
    )
}
