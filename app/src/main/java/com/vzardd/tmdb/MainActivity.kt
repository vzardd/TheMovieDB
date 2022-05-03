package com.vzardd.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vzardd.tmdb.navigation.AppNavigation
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.viewmodel.MovieViewModel
import com.vzardd.tmdb.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val searchViewModel: SearchViewModel = viewModel()
            val movieViewModel: MovieViewModel = viewModel()
            TheMovieDBTheme {
                AppNavigation(searchViewModel, movieViewModel)
            }
        }
    }
}
