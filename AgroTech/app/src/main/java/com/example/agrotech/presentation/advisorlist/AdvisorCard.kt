package com.example.agrotech.presentation.advisorlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.agrotech.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

data class AdvisorCard(
    val id: Long,
    val name: String,
    val occupation: String,
    val rating: Double,
    val link: String
)

@Composable
fun AdvisorCard(advisor: AdvisorCard, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        colors = CardColors(
            contentColor = Color.White,
            containerColor = Color(0xFFF4B696),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFF4B696),
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier = Modifier.size(128.dp).clip(CircleShape),
                imageModel = {
                    advisor.link.ifBlank { R.drawable.placeholder }
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            Text(
                text = advisor.name,
                color = Color(0xFF222B45),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "⭐ ${advisor.rating}",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
