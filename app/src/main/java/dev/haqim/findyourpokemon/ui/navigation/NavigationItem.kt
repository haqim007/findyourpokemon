package dev.haqim.catapp.ui.screen.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import dev.haqim.findyourpokemon.ui.navigation.Screen

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen,
)
