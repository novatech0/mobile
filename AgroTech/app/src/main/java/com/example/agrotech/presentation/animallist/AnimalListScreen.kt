package com.example.agrotech.presentation.animallist

import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotech.domain.animal.Animal

@Composable
fun AnimalListScreen(viewModel: AnimalListViewModel, enclosureId: Long) {
    val state = viewModel.state
    val enclosure = viewModel.enclosure
    val showAnimalDialog = viewModel.showAnimalDialog
    val showEnclosureDialog = viewModel.showEnclosureDialog
    val isExpanded = viewModel.expanded
    val healthStatusTranslation = mapOf(
        "HEALTHY" to "Saludable",
        "SICK" to "Enfermo",
        "DEAD" to "Muerto",
        "UNKNOWN" to "Desconocido"
    )

    LaunchedEffect(Unit) {
        viewModel.getEnclosure(enclosureId)
        viewModel.getAnimals(enclosureId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.setShowAnimalDialog(true) },containerColor = Color(0xFF092C4C),
                contentColor = Color.White
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
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
                        text = if (enclosure.isLoading) "Mi recinto" else (enclosure.data?.name ?: "Mi recinto"),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                IconButton(onClick = {
                    viewModel.setExpanded(true)
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier.padding(horizontal = 8.dp).size(32.dp)
                    )
                }
                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = { viewModel.setExpanded(false) },
                    offset = DpOffset(x = (2000).dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Editar recinto",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        onClick = {
                            viewModel.setShowEnclosureDialog(true)
                            viewModel.setExpanded(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Eliminar recinto",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        onClick = {
                            viewModel.deleteEnclosure(enclosureId)
                            viewModel.setExpanded(false)
                        }
                    )
                }
            }
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.data.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message.ifEmpty { "No se encontraron animales." },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    state.data.let { animals ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            animals.forEach { animal ->
                                AnimalListItem(
                                    animal = animal,
                                    healthStatusTranslation = healthStatusTranslation,
                                    onClick = { viewModel.goToAnimalDetails(animal.id) }
                                )
                            }
                        }
                    }
                }
            }
            if (showAnimalDialog) {
                AnimalDialog(
                    viewModel = viewModel,
                    healthStatusTranslation=healthStatusTranslation,
                    enclosureId = enclosureId
                )
            }
            if (showEnclosureDialog) {
                EnclosureDialog(
                    viewModel = viewModel,
                    enclosureId = enclosureId
                )
            }
        }
    }
}

@Composable
fun AnimalListItem(
    animal: Animal,
    healthStatusTranslation: Map<String, String> = mapOf(),
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🐮 ${animal.name}", fontWeight = FontWeight.Bold)
            Text("🎂 Edad: ${animal.age} años")
            Text("🧬 Especie: ${animal.species}")
            Text("🩺 Estado de salud: ${healthStatusTranslation[animal.health] ?: animal.health}")
        }
    }
}

@Composable
private fun AnimalDialog(viewModel: AnimalListViewModel, healthStatusTranslation: Map<String, String> = mapOf(), enclosureId: Long) {
    val genderTranslation = mapOf(
        true to "Macho",
        false to "Hembra"
    )
    AlertDialog(
        onDismissRequest = { viewModel.setShowAnimalDialog(false) },
        title = { Text("Añadir Animal") },
        text = {
            Column {
                OutlinedTextField(
                    value = viewModel.animalName,
                    onValueChange = { viewModel.setAnimalName(it) },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.animalAge.toString(),
                    onValueChange = { viewModel.setAnimalAge(it.toIntOrNull() ?: 0) },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.animalSpecies,
                    onValueChange = { viewModel.setAnimalSpecies(it) },
                    label = { Text("Especie") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.animalBreed,
                    onValueChange = { viewModel.setAnimalBreed(it) },
                    label = { Text("Raza") },
                    modifier = Modifier.fillMaxWidth()
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
                        val genderText = genderTranslation[viewModel.animalGender] ?: "Género"
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
                                viewModel.setAnimalGender(true)
                                viewModel.setGenderExpanded(false)
                            },
                            text = { Text("Macho") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                viewModel.setAnimalGender(false)
                                viewModel.setGenderExpanded(false)
                            },
                            text = { Text("Hembra") }
                        )
                    }
                }
                OutlinedTextField(
                    value = viewModel.animalWeight.toString(),
                    onValueChange = { viewModel.setAnimalWeight(it.toFloatOrNull() ?: 0f) },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth()
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
                        .clickable { viewModel.setAnimalHealthExpanded(true) }
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val text = healthStatusTranslation[viewModel.animalHealthStatus] ?: "Estado de Salud"
                        Text(
                            text = text,
                            color = if (viewModel.animalHealthStatus.isEmpty()) Color.Gray else Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expandir"
                        )
                    }
                }
                DropdownMenu(
                    expanded = viewModel.animalHealthExpanded.value,
                    onDismissRequest = { viewModel.setAnimalHealthExpanded(false) },
                ) {
                    listOf("HEALTHY", "SICK", "DEAD", "UNKNOWN").forEach { status ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.setAnimalHealthStatus(status)
                                viewModel.setAnimalHealthExpanded(false)
                            },
                            text = { Text(healthStatusTranslation[status] ?: status)  }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.createAnimal(enclosureId)
                viewModel.setShowAnimalDialog(false)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60), // Verde
                    contentColor = Color.White // Texto blanco
                )) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.setShowAnimalDialog(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84343), // Rojo
                    contentColor = Color.White // Texto blanco
                )) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EnclosureDialog(viewModel: AnimalListViewModel, enclosureId: Long) {
    AlertDialog(
        onDismissRequest = { viewModel.setShowEnclosureDialog(false) },
        title = { Text("Editar Recinto") },
        text = {
            Column {
                OutlinedTextField(
                    value = viewModel.enclosureName,
                    onValueChange = { viewModel.setEnclosureName(it) },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.enclosureCapacity.toString(),
                    onValueChange = { viewModel.setEnclosureCapacity(it.toIntOrNull() ?: 0) },
                    label = { Text("Capacidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.enclosureType,
                    onValueChange = { viewModel.setEnclosureType(it) },
                    label = { Text("Tipo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.editEnclosure(enclosureId)
                viewModel.setShowEnclosureDialog(false)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60), // Verde
                    contentColor = Color.White // Texto blanco
                )) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.setShowEnclosureDialog(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84343), // Rojo
                    contentColor = Color.White // Texto blanco
                )) {
                Text("Cancelar")
            }
        }
    )
}