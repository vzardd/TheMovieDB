package com.vzardd.tmdb.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vzardd.tmdb.R
import com.vzardd.tmdb.datastore.MovieDataStore
import com.vzardd.tmdb.navigation.AppScreens
import com.vzardd.tmdb.room.MovieDetails
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.uicomponents.MovieRowCard
import com.vzardd.tmdb.viewmodel.MovieViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun FavouritesScreen(navController: NavController, movieViewModel: MovieViewModel) {
    val movieDatastore = MovieDataStore(LocalContext.current)
    val movieList = remember{
        mutableStateOf(emptyList<MovieDetails>())
    }
    LaunchedEffect(Unit){
        val movieListIds = movieDatastore.getFavListIds()
        movieList.value = movieViewModel.getFavouritesByIds(movieListIds)
    }
    TheMovieDBTheme {
        Scaffold(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Icon(modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.popBackStack()
                            },imageVector = Icons.Default.ArrowBack, contentDescription = "back", tint = MaterialTheme.colors.onSecondary)
                        Text(text = "Favourites", color = MaterialTheme.colors.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Image(modifier = Modifier.size(40.dp),painter = painterResource(id = R.drawable.moviedb_logo), contentDescription = "logo")
                    }

                }
            }
        ) {
            if(movieList.value.isEmpty()){
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),text = "Nothing to show :)", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
            }
            else{
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)){
                    items(movieList.value){
                        
                        movie ->
                        //on click navigate to movie screen
                        MovieRowCard(movie.id, movie.poster_path,movie.title,movie.release_date,movie.overview){
                            navController.navigate(AppScreens.MovieScreen.name+"/${movie.id}")
                        }
                    }
                }
            }
        }
    }
}