package com.example.agrotech.presentation.reviewlist

import android.widget.ImageView
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.agrotech.R

@Composable
fun ReviewListScreen(
    navController: NavController,
    viewModel: ReviewListViewModel,
    advisorId: Long) {
    val state = viewModel.state.value
    val advisor = viewModel.advisorCard.value

    LaunchedEffect(Unit) {
        viewModel.getAdvisorReviewList(advisorId)
        viewModel.getAdvisorDetail(advisorId)
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
                        text = "Reseñas",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                IconButton(
                    onClick = { /* Acción de más opciones */ }
                    ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
            }
            if (state.isLoading || advisor.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardColors(
                        contentColor = Color.White,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color(0xFFBAC2CB)
                    )) {
                    Column (
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = advisor.data?.link,
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFF4B696), CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.placeholder),
                            error = painterResource(R.drawable.placeholder)
                        )
                        advisor.data?.let {
                            Text(
                                text = it.name,
                                color = Color(0xFF222B45),
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                        Card(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                            colors = CardColors(
                                contentColor = Color.White,
                                containerColor = Color(0xFFFFFFFF),
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color(0xFFBAC2CB)
                            )) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "⭐ ${advisor.data?.rating}",
                                color = Color(0xFFF7C480),
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
                if (state.data.isNullOrEmpty()) {
                    Text(
                        text = state.message.ifEmpty { "No hay reseñas sobre este asesor" },
                        modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF222B45),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        state.data.let {
                            items(count = it.size, itemContent = { index ->
                                ReviewCard(it[index])
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: ReviewCard) {
    val rating = review.rating
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = Color(0xFFF9EAE1),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFF4B696),
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            AsyncImage(
                model = review.farmerLink,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )
            Text(
                text = review.farmerName,
                color = Color(0xFF222B45),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Row {
                for (i in 1..rating) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFD9A722)
                    )
                }
            }
            Text(
                text = review.comment,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            )
        }
    }
}