package com.example.coursach.navigation_bar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = "Screen_1_shop"){
        composable("Screen_1_shop"){
            Screen1(navController = navHostController)
        }
        composable("Screen_2_rent"){
            Screen2(navController = navHostController)
        }
        composable("Screen_3_repair"){
            Screen3(navController = navHostController)
        }
        composable("Screen_4_account"){
            Screen4()
        }
        composable("OpenCarScreen/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: -1
            OpenCarShopScreen(carId)
        }
        composable("OpenCarRentScreen/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: -1
            OpenCarRentScreen(carId)
        }
    }
}