package com.example.agrotech.presentation.advisorlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdvisorListScreen(viewModel: AdvisorListViewModel) {
    val state = viewModel.state.value
    val filter = viewModel.filter.value
    val search = viewModel.search.value

    LaunchedEffect(Unit) {
        viewModel.getAdvisorList()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(16.dp)
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
                        text = "Asesores",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Row {
                FilterChip(
                    modifier = Modifier.padding(8.dp),
                    selected = filter == "Nombre",
                    onClick = { viewModel.onFilterChange("Nombre") },
                    label = { Text("Nombre")}
                )
                FilterChip(
                    modifier = Modifier.padding(8.dp),
                    selected = filter == "Ocupación",
                    onClick = { viewModel.onFilterChange("Ocupación") },
                    label = { Text("Ocupación")}
                )
                FilterChip(
                    modifier = Modifier.padding(8.dp),
                    selected = filter == "Calificación",
                    onClick = { viewModel.onFilterChange("Calificación") },
                    label = { Text("Calificación")}
                )
            }
            TextField(
                value = search,
                onValueChange = {
                    viewModel.onSearchChange(it)
                    viewModel.filterAdvisorList() },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).size(52.dp),
                placeholder = {
                    Text(
                        text = "Busca asesores",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                state.data?.let {
                    items(count = it.size, itemContent = { index ->
                        AdvisorCard(advisor = it[index], onClick = {
                            viewModel.goToAdvisorProfile(it[index].id)
                        })
                    })
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (state.message.isNotBlank()) {
                Text(
                    text = state.message,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}