package com.zahra.space.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zahra.space.ui.screens.splash.SplashScreen
import com.zahra.space.ui.screens.onboarding.OnboardingScreen
import com.zahra.space.ui.screens.dashboard.DashboardScreen
import com.zahra.space.ui.screens.quran.QuranHomeScreen
import com.zahra.space.ui.screens.quran.QuranReadScreen
import com.zahra.space.ui.screens.quran.QuranHafalanScreen
import com.zahra.space.ui.screens.dzikir.DzikirHomeScreen
import com.zahra.space.ui.screens.dzikir.DzikirCounterScreen
import com.zahra.space.ui.screens.checklist.ChecklistScreen
import com.zahra.space.ui.screens.todo.TodoHomeScreen
import com.zahra.space.ui.screens.todo.TodoDetailScreen
import com.zahra.space.ui.screens.todo.TodoCreateScreen
import com.zahra.space.ui.screens.fitness.FitnessHomeScreen
import com.zahra.space.ui.screens.pet.PetHomeScreen
import com.zahra.space.ui.screens.pet.PetShopScreen
import com.zahra.space.ui.screens.game.GameWorldScreen
import com.zahra.space.ui.screens.profile.ProfileScreen
import com.zahra.space.ui.screens.profile.SettingsScreen
import com.zahra.space.ui.screens.letters.MonthlyLetterScreen
import com.zahra.space.viewmodel.OnboardingViewModel

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Splash.route) {
    NavHost(navController, startDestination) {
        composable(Screen.Splash.route) {
            SplashScreen { navController.navigate(Screen.Onboarding.route) { popUpTo(Screen.Splash.route) { inclusive = true } } }
        }
        composable(Screen.Onboarding.route) {
            val vm: OnboardingViewModel = hiltViewModel()
            val isComplete by vm.isOnboardingComplete.collectAsState()
            OnboardingScreen { vm.saveUser(it) }
            if (isComplete) {
                navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
            }
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToQuran = { navController.navigate(Screen.Quran.route) },
                onNavigateToDzikir = { navController.navigate(Screen.Dzikir.route) },
                onNavigateToChecklist = { navController.navigate(Screen.Checklist.route) },
                onNavigateToTodo = { navController.navigate(Screen.Todo.route) },
                onNavigateToFitness = { navController.navigate(Screen.Fitness.route) },
                onNavigateToPet = { navController.navigate(Screen.Pet.route) },
                onNavigateToGame = { navController.navigate(Screen.Game.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToLetters = { navController.navigate(Screen.Letters.route) }
            )
        }
        composable(Screen.Quran.route) {
            QuranHomeScreen(
                navController = navController,
                onNavigateToRead = { surahId, verseId -> navController.navigate("quran_read/$surahId/$verseId") },
                onNavigateToHafalan = { surahId -> navController.navigate("quran_hafalan/$surahId") }
            )
        }
        composable("quran_read/{surahId}/{verseId}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            val verseId = backStackEntry.arguments?.getString("verseId")?.toIntOrNull() ?: 1
            QuranReadScreen(surahId, verseId)
        }
        composable("quran_hafalan/{surahId}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            QuranHafalanScreen(surahId)
        }
        composable(Screen.Dzikir.route) {
            DzikirHomeScreen { dzikirId -> navController.navigate("dzikir_counter/$dzikirId") }
        }
        composable("dzikir_counter/{dzikirId}") { backStackEntry ->
            val dzikirId = backStackEntry.arguments?.getString("dzikirId")?.toLongOrNull() ?: 1
            DzikirCounterScreen(dzikirId)
        }
        composable(Screen.Checklist.route) { ChecklistScreen() }
        composable(Screen.Todo.route) {
            TodoHomeScreen(
                onNavigateToDetail = { todoId -> navController.navigate("todo_detail/$todoId") },
                onNavigateToCreate = { navController.navigate("todo_create") }
            )
        }
        composable("todo_detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toLongOrNull() ?: 0
            TodoDetailScreen(todoId)
        }
        composable("todo_create") { TodoCreateScreen { navController.popBackStack() } }
        composable(Screen.Fitness.route) { FitnessHomeScreen() }
        composable(Screen.Pet.route) { PetHomeScreen { navController.navigate(Screen.PetShop.route) } }
        composable(Screen.PetShop.route) { PetShopScreen() }
        composable(Screen.Game.route) { GameWorldScreen() }
        composable(Screen.Profile.route) { ProfileScreen(
            onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
            onNavigateToLetters = { navController.navigate(Screen.Letters.route) }
        ) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Letters.route) { MonthlyLetterScreen() }
    }
}
