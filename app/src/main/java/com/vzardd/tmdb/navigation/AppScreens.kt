package com.vzardd.tmdb.navigation

import java.lang.IllegalArgumentException

enum class AppScreens {
    HomeScreen,
    CategoryScreen,
    MovieScreen,
    SearchScreen,
    FavouritesScreen;
    companion object{
        fun fromRoute(route: String?): AppScreens{
            return when(route?.substringBefore("/")){
                HomeScreen.name -> HomeScreen
                CategoryScreen.name -> CategoryScreen
                MovieScreen.name -> MovieScreen
                SearchScreen.name -> SearchScreen
                FavouritesScreen.name -> FavouritesScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Invalid route, $route")
            }
        }
    }
}