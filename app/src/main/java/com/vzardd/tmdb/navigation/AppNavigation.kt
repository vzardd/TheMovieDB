package com.vzardd.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vzardd.tmdb.screens.*
import com.vzardd.tmdb.viewmodel.MovieViewModel
import com.vzardd.tmdb.viewmodel.SearchViewModel

@Composable
fun AppNavigation(searchViewModel: SearchViewModel, movieViewModel: MovieViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.name){
        composable(AppScreens.HomeScreen.name){
            HomeScreen(navController, movieViewModel)
        }
        composable(
            AppScreens.CategoryScreen.name+"/{category}",
            arguments = listOf(navArgument("category"){ type = NavType.StringType})
        ){
            CategoryScreen(navController, category = it.arguments?.getString("category"), movieViewModel)
        }
        composable(
            AppScreens.MovieScreen.name+"/{id}",
            arguments = listOf(navArgument("id"){ type = NavType.IntType})
        ){
            MovieScreen(navController, it.arguments?.getInt("id"), movieViewModel)
        }
        composable(
            AppScreens.SearchScreen.name
        ){
            SearchScreen(navController, searchViewModel)
        }
        composable(
            AppScreens.FavouritesScreen.name
        ){
            FavouritesScreen(navController, movieViewModel)
        }
    }
}