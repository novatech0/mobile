package com.example.agrotech.presentation.navigationcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class CardItem(
    val image: Painter,
    val text: String,
    val onClick: () -> Unit = {}
)

@Composable
fun NavigationCard(index : Int, cardItems: List<CardItem>) {
    Card(
        modifier = Modifier.width(160.dp).height(200.dp).padding(bottom = 16.dp),
        colors = CardColors(
            contentColor = Color.White,
            containerColor = Color(0xFFF4B696),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFF4B696),
        ),
        onClick = { cardItems[index].onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = cardItems[index].image,
                contentDescription = cardItems[index].text,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Text(
                text = cardItems[index].text,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}