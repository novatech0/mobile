package com.example.agrotech.presentation.exploreposts

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.agrotech.R
import com.example.agrotech.common.Routes

@Composable
fun ExplorePostsScreen(
    navController: NavController,
    viewModel: ExplorePostsViewModel) {
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.getPostList()
    }

    Scaffold { paddingValues ->
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
                        text = "Explorar",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier.padding(horizontal = 8.dp).size(32.dp)
                    )
                }
            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when {
                    state.data != null -> {
                        state.data.forEach { post ->
                            PostCardItem(post = post) {
                                navController.navigate(Routes.AdvisorDetail.route + "/${post.advisorId}")
                            }
                        }
                    }
                    state.message.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun PostCardItem(post: PostCard, onClick : () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                onClick()
            }
        ) {
            AsyncImage(
                model = post.advisorPhoto,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )

            Text(
                text = post.advisorName,
                color = Color(0xFF222B45),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
            )
        }
        AsyncImage(
            model = post.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder)
        )


        Text(
            text = post.title,
            color = Color(0xFF222B45),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = post.description,
            color = Color(0xFF222B45),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }

}