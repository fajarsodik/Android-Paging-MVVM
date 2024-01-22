package com.hevadevelop.jetpackcomposemvvmpaging.ui.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hevadevelop.jetpackcomposemvvmpaging.ui.details.DetailNewsScreen
import com.hevadevelop.jetpackcomposemvvmpaging.ui.home.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable(
            route = "detail_news_screen/{url_news}",
            arguments = listOf(navArgument("url_news") {
                type = NavType.StringType
            })
        ) {
            val url_news = it.arguments?.getString("url_news") ?: ""
            DetailNewsScreen(url_news, navController)
        }
    }
}