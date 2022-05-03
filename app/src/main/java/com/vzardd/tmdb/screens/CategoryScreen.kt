package com.vzardd.tmdb.screens

import android.os.Bundle
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.vzardd.tmdb.R
import com.vzardd.tmdb.model.MoviesList
import com.vzardd.tmdb.model.Result
import com.vzardd.tmdb.navigation.AppScreens
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.uicomponents.LoadingBox
import com.vzardd.tmdb.uicomponents.MovieRowCard
import com.vzardd.tmdb.util.DataOrException
import java.lang.Exception
import java.lang.IllegalArgumentException

@Composable
fun CategoryScreen(
    navController: NavHostController,
    category: String?,
    movieData: MutableState<DataOrException<MoviesList?, Boolean?, Exception>>
) {
    val title = when(category){
        "popular" -> "Popular Movies"
        "top_rated" -> "Top Rated"
        "now_playing" -> "Now Playing"
        "upcoming" -> "Upcoming Movies"
        else -> throw IllegalArgumentException("Invalid parameter")
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
                        Text(text = title, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Image(modifier = Modifier.size(40.dp),painter = painterResource(id = R.drawable.moviedb_logo), contentDescription = "logo")
                    }

                }
            }
        ) {

            if(movieData.value.e != null){
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),text = "Something went wrong :(  Check your internet connection and try again!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
            }
            else if(movieData.value.loading == true){
                LoadingBox()
            }
            else{
                val moviesList = movieData.value.data?.results?: emptyList()
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)){
                    items(moviesList){
                        MovieRowCard(id = it.id, poster = it.poster_path?:"", movieTitle = it.title?:"Untitled", releaseDate = it.release_date?:" ", overview = it.overview?:"Info Unavailable."){
                            navController.navigate(AppScreens.MovieScreen.name + "/${it}")
                        }
                    }
                }
            }
        }
    }
}