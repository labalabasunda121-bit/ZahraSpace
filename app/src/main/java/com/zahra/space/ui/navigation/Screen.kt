package com.zahra.space.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object Quran : Screen("quran")
    data object QuranRead : Screen("quran_read/{surahId}/{verseId}")
    data object QuranHafalan : Screen("quran_hafalan/{surahId}")
    data object Dzikir : Screen("dzikir")
    data object DzikirCounter : Screen("dzikir_counter/{dzikirId}")
    data object Checklist : Screen("checklist")
    data object Todo : Screen("todo")
    data object TodoDetail : Screen("todo_detail/{todoId}")
    data object TodoCreate : Screen("todo_create")
    data object Fitness : Screen("fitness")
    data object Pet : Screen("pet")
    data object PetShop : Screen("pet_shop")
    data object Game : Screen("game")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Letters : Screen("letters")
    
    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
