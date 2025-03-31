package com.example.cardsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cardsapp.R
import com.example.cardsapp.navigation.Screen

data class HomeCard(
    val title: String,
    val description: String,
    val imageResId: Int,
    val route: String
)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val cards = listOf(
        HomeCard(
            "Tarjetas para Pareja",
            "Fortalece tu relación con preguntas divertidas y profundas",
            R.drawable.couple_cards,
            Screen.Cards.route
        ),
        HomeCard(
            "Próximamente",
            "Nuevas funcionalidades en desarrollo",
            R.drawable.placeholder,
            Screen.Cards.route
        ),
        HomeCard(
            "Próximamente",
            "Nuevas funcionalidades en desarrollo",
            R.drawable.placeholder,
            Screen.Cards.route
        ),
        HomeCard(
            "Próximamente",
            "Nuevas funcionalidades en desarrollo",
            R.drawable.placeholder,
            Screen.Cards.route
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cards) { card ->
            HomeCardItem(card = card, onClick = { onNavigate(card.route) })
        }
    }
}

@Composable
fun HomeCardItem(card: HomeCard, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant,  // Usa un color del tema
            contentColor = colorScheme.onSurfaceVariant   // Color de texto adecuado para ese fondo
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = card.imageResId),
                colorFilter = ColorFilter.tint(colorScheme.onSurfaceVariant),
                contentDescription = card.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = card.description,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 