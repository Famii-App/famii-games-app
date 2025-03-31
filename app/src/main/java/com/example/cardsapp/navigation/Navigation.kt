package com.example.cardsapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cards : Screen("cards")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

enum class BottomNavItem(val route: String, val title: String, val icon: String) {
    Home(Screen.Home.route, "Inicio", "home"),
    Cards(Screen.Cards.route, "Tarjetas", "cards"),
    Profile(Screen.Profile.route, "Perfil", "person"),
    Settings(Screen.Settings.route, "Ajustes", "settings");

    companion object {
        fun values(): List<BottomNavItem> = listOf(Home, Cards, Profile, Settings)
    }
} 