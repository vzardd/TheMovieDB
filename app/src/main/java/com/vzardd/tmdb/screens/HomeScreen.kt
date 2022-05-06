package com.vzardd.tmdb.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.vzardd.tmdb.R
import com.vzardd.tmdb.navigation.AppScreens
import com.vzardd.tmdb.room.MovieDetails
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.uicomponents.LoadingBox
import com.vzardd.tmdb.util.Constants
import com.vzardd.tmdb.util.TMDBUtil
import com.vzardd.tmdb.viewmodel.MovieViewModel

@Composable
fun HomeScreen(navController: NavHostController, movieViewModel: MovieViewModel) {
    TheMovieDBTheme {
        val drawerState = remember{
            mutableStateOf(false)
        }

        val popularMovies = movieViewModel.popularMovies.collectAsState().value
        val topRatedMovies = movieViewModel.topRatedMovies.collectAsState().value
        val nowPlayingMovies = movieViewModel.nowPlayingMovies.collectAsState().value
        val upcomingMovies = movieViewModel.upcomingMovies.collectAsState().value

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                drawerState.value = false
            },
            topBar = {
                CustomTopBar{
                    drawerState.value = !drawerState.value
                }
            }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)){
                if(popularMovies.isNullOrEmpty() || topRatedMovies.isNullOrEmpty() || nowPlayingMovies.isNullOrEmpty() || upcomingMovies.isNullOrEmpty()){
                    MainContent(
                        navController = navController,
                        popularMovies = popularMovies,
                        topRatedMovies = topRatedMovies,
                        nowPlayingMovies = nowPlayingMovies,
                        upcomingMovies = upcomingMovies,
                        problem = true
                    )
                }
                else{
                    MainContent(
                        navController = navController,
                        popularMovies = popularMovies,
                        topRatedMovies = topRatedMovies,
                        nowPlayingMovies = nowPlayingMovies,
                        upcomingMovies = upcomingMovies,
                        problem = false
                    )
                }
                NavigationDrawer(drawerState.value, navController = navController)
            }
        }
    }
}

@Composable
fun NavigationDrawer(value: Boolean, navController: NavController) {
    AnimatedVisibility(visible = value) {
        Row(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.75f)
                .background(MaterialTheme.colors.primary)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.CategoryScreen.name + "/popular")
                    },text = "Popular Movies", textAlign =  TextAlign.Center,color = MaterialTheme.colors.onPrimary, fontSize = 22.sp)
                Text(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.CategoryScreen.name + "/top_rated")
                    },text = "Top Rated Movies", textAlign =  TextAlign.Center, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp)
                Text(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.CategoryScreen.name + "/now_playing")
                    },text = "Now Playing", textAlign =  TextAlign.Center, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp)
                Text(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.CategoryScreen.name + "/upcoming")
                    },text = "Upcoming Movies", textAlign =  TextAlign.Center, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp)
                Text(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.FavouritesScreen.name)
                    },text = "Favourites",textAlign =  TextAlign.Center, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp)
            }
        }
    }
}

//to show popular, now playing, top rated etc..
@Composable
fun MainContent(navController: NavController, popularMovies: List<MovieDetails>, topRatedMovies: List<MovieDetails>, nowPlayingMovies: List<MovieDetails>, upcomingMovies: List<MovieDetails>, problem: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeBox(navController = navController)
        if(problem){
            if(TMDBUtil.isNetworkAvailable(LocalContext.current)){
                LoadingBox()
            }
            else{
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),text = "Nothing to show :(  Check your internet connection and try again!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
            }
        }
        else {
            if (popularMovies.isEmpty()) {
                LoadingBox()
            } else {
                CategoryMovies(
                    "What's Popular",
                    navController = navController,
                    popularMovies
                )
                Divider()
            }
            if (topRatedMovies.isEmpty()) {
                LoadingBox()
            } else {
                CategoryMovies(
                    "Top Rated",
                    navController = navController,
                    topRatedMovies
                )
                Divider()
            }
            if (nowPlayingMovies.isEmpty()) {
                LoadingBox()
            } else {
                CategoryMovies(
                    "Now Playing",
                    navController = navController,
                    nowPlayingMovies
                )
                Divider()
            }
            if (upcomingMovies.isEmpty()) {
                LoadingBox()
            } else {
                CategoryMovies(
                    "Upcoming Movies",
                    navController = navController,
                    upcomingMovies
                )
            }
        }
    }
}


@Composable
fun WelcomeBox(navController: NavController){
    Box(
        Modifier
            .fillMaxWidth()
            .height(350.dp)
            //.aspectRatio(1.4f)
    ){
        Image(painter = painterResource(id = R.drawable.homepage), contentDescription = "Home page", contentScale = ContentScale.Crop)
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    alpha = 0.7f, brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.primaryVariant
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Text(text = "Welcome.", color = MaterialTheme.colors.onPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Millions of Movies to discover. Explore now.", color = MaterialTheme.colors.onPrimary, fontFamily = FontFamily.SansSerif, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(30.dp))
            SearchCard(navController = navController)
        }
    }
}

// arg - String, List<Result>
@Composable
fun CategoryMovies(category: String, navController: NavController, list: List<MovieDetails>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(10.dp, 20.dp, 0.dp, 10.dp)) {
        TitleText(text = category)
        Box(modifier = Modifier.fillMaxWidth()){
            LazyRow(
                Modifier.fillMaxWidth()
            ){
                items(list){
                    //Movie Item Card
                    ImageCard(navController = navController, it.title?: "Untitled", it.poster_path?:"", it.id?:0, it.release_date?:" ", it.vote_average?: 0.0)
                }
            }
        }
    }
}


//arg - Result
@Composable
fun ImageCard(navController: NavController, title: String, poster: String, id: Int, release_date: String, vote_avg: Double){
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .width(220.dp)
            .padding(10.dp)){
        Card(
            Modifier
                .width(200.dp)
                .height(300.dp)
                .clickable {
                    navController.navigate(AppScreens.MovieScreen.name + "/${id}") // pass movie id here
                }, elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
            Box(Modifier.fillMaxSize()){
                //must be updated
                if(poster.isEmpty()){
                    Image(painter = painterResource(id = R.drawable.default_poster), contentDescription = "movie poster", contentScale = ContentScale.Crop)
                }
                else{
                    Image(modifier = Modifier.fillMaxSize(),painter = rememberAsyncImagePainter(
                        model = Constants.IMAGE_BASE_URL+poster, placeholder = painterResource(id = R.drawable.default_poster)
                    ), contentDescription = "movie poster", contentScale = ContentScale.Crop)
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp), Alignment.BottomStart) {
                    Card(shape = CircleShape){
                        Row(
                            Modifier
                                .background(MaterialTheme.colors.onBackground)
                                .padding(10.dp), verticalAlignment = Alignment.CenterVertically){
                            //must be updated
                            Text(text = "${(vote_avg*10).toInt()}%", color = MaterialTheme.colors.onPrimary, fontWeight = FontWeight.Bold)
                            Image(modifier = Modifier
                                .padding(5.dp)
                                .size(20.dp),painter = painterResource(id = R.drawable.heart), contentDescription = "loves")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        //Must be changed
        Text(text = title, color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        //Must be changed
        Text(text = TMDBUtil.dateFormatter(release_date), color = MaterialTheme.colors.secondaryVariant, fontSize = 18.sp)
    }
}

@Composable
fun TitleText(text: String){
    Text(modifier = Modifier.padding(10.dp,0.dp,0.dp,10.dp),text = text, color = MaterialTheme.colors.onBackground, fontSize = 26.sp, fontWeight = FontWeight(500), fontFamily = FontFamily.SansSerif)
}

@Composable
fun SearchCard(navController: NavController){

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .clickable {
            navController.navigate(AppScreens.SearchScreen.name)
        }, elevation = 10.dp, shape = CircleShape, backgroundColor = MaterialTheme.colors.background) {
        Row(
            Modifier
                .fillMaxWidth()
                //.defaultMinSize(0.dp, 100.dp)
                .padding(20.dp, 0.dp, 0.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(4f),text = "Search...", color = MaterialTheme.colors.onSecondary, fontSize = 22.sp)
            Card(modifier = Modifier.weight(2f),elevation = 10.dp, shape = CircleShape) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colors.secondary,
                                MaterialTheme.colors.primaryVariant
                            )
                        )
                    ), Alignment.Center){
                    Text(
                        text = "Search",
                        Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.background,
                        textAlign = TextAlign.Center
                    )
                }

            }

        }

    }
}

@Composable
fun CustomTopBar(updateDrawerState: () -> Unit){
    TopAppBar(elevation = 10.dp) {
        Box(Modifier.background(MaterialTheme.colors.primary)){
            Icon(modifier = Modifier
                .padding(10.dp)
                .size(35.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    updateDrawerState()
                },imageVector = Icons.Default.Menu, contentDescription = "menu", tint = Color.White)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(modifier = Modifier.size(40.dp),painter = painterResource(id = R.drawable.moviedb_logo), contentDescription = "logo")
            }
        }
    }
}