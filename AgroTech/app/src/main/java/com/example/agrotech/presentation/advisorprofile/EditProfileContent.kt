package com.example.agrotech.presentation.advisorprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.agrotech.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun EditProfileContent(viewModel: AdvisorProfileViewModel) {
    val firstName = viewModel.firstName
    val lastName = viewModel.lastName
    val birthDate = viewModel.birthDate
    val photo = viewModel.photo
    val city = viewModel.city
    val country = viewModel.country
    val occupation = viewModel.occupation
    val experience = viewModel.experience
    val description = viewModel.description

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.updateProfileWithImage(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    }
            ) {
                GlideImage(
                    imageModel = {
                        photo.value.ifBlank { R.drawable.placeholder }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset((-8).dp, (-8).dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .padding(4.dp)
                        .zIndex(2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Image",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = firstName.value,
                    onValueChange = { viewModel.onFirstNameChange(it) },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    value = lastName.value,
                    onValueChange = { viewModel.onLastNameChange(it) },
                    label = { Text("Apellido") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = birthDate.value,
                onValueChange = { viewModel.onBirthDateChange(it) },
                label = { Text("Fecha de nacimiento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = city.value,
                    onValueChange = { viewModel.onCityChange(it) },
                    label = { Text("Ciudad") },
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    value = country.value,
                    onValueChange = { viewModel.onCountryChange(it) },
                    label = { Text("País") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = occupation.value,
                    onValueChange = { viewModel.onOccupationChange(it) },
                    label = { Text("Ocupación") },
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    value = experience.value.toString(),
                    onValueChange = { viewModel.onExperienceChange(it.toIntOrNull() ?: 0) },
                    label = { Text("Experiencia (años)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = description.value,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

        }

        Button(
            onClick = {
                viewModel.updateAdvisorProfile()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E64FF)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
        ) {
            Text(text = "Guardar", color = Color.White, fontSize = 16.sp)
        }
    }
}