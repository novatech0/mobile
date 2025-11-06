package com.example.agrotech.presentation.animaldetails

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimalDetailsScreen(viewModel: AnimalDetailsViewModel, animalId: Long) {
    val state = viewModel.state
    val isExpanded = viewModel.expanded.value
    val healthStatusTranslation = mapOf(
        "HEALTHY" to "Saludable",
        "SICK" to "Enfermo",
        "DEAD" to "Muerto",
        "UNKNOWN" to "Desconocido"
    )

    val genderTranslation = mapOf(
        true to "Macho",
        false to "Hembra"
    )

    LaunchedEffect(Unit) {
        viewModel.getAnimal(animalId)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
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
                        text = if (state.isLoading) "Detalles de Animal" else (state.data?.name ?: "Detalles de Animal"),
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
                                text = "Eliminar",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        onClick = {
                            viewModel.deleteAnimal(animalId)
                        }
                    )
                }
            }
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.data != null -> {
                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.setName(it) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.age.toString(),
                        onValueChange = {
                            val value = it.toIntOrNull() ?: 0
                            viewModel.setAge(value)
                        },
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = viewModel.species,
                        onValueChange = { viewModel.setSpecies(it) },
                        label = { Text("Especie") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.breed,
                        onValueChange = { viewModel.setBreed(it) },
                        label = { Text("Raza") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                            .clickable { viewModel.setGenderExpanded(true) }
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val genderText = genderTranslation[viewModel.gender] ?: "Género"
                            Text(
                                text = genderText,
                                color = Color.Black
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir")
                        }

                        DropdownMenu(
                            expanded = viewModel.genderExpanded.value,
                            onDismissRequest = { viewModel.setGenderExpanded(false) }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.setGender(true)
                                    viewModel.setGenderExpanded(false)
                                },
                                text = { Text("Macho") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.setGender(false)
                                    viewModel.setGenderExpanded(false)
                                },
                                text = { Text("Hembra") }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = viewModel.weight.toString(),
                        onValueChange = {
                            val value = it.toFloatOrNull() ?: 0f
                            viewModel.setWeight(value)
                        },
                        label = { Text("Peso") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { viewModel.setHealthStatusExpanded(true) }
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val text = healthStatusTranslation[viewModel.health] ?: "Estado de Salud"
                            Text(
                                text = text,
                                color = if (viewModel.health.isEmpty()) Color.Gray else Color.Black
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expandir"
                            )
                        }
                        DropdownMenu(
                            expanded = viewModel.healthStatusExpanded.value,
                            onDismissRequest = { viewModel.setHealthStatusExpanded(false) },
                        ) {
                            listOf("HEALTHY", "SICK", "DEAD", "UNKNOWN").forEach { status ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.setHealth(status)
                                        viewModel.setHealthStatusExpanded(false)
                                    },
                                    text = { Text(healthStatusTranslation[status] ?: status) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    Button(
                        onClick = { viewModel.updateAnimal(animalId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar")
                    }
                }
                state.message.isNotBlank() -> {
                    Text(
                        text = state.message,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}