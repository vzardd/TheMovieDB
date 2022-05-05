package com.vzardd.tmdb.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.vzardd.tmdb.R
import com.vzardd.tmdb.datastore.MovieDataStore
import com.vzardd.tmdb.model.Cast
import com.vzardd.tmdb.model.Genre
import com.vzardd.tmdb.model.Keyword
import com.vzardd.tmdb.model.Result
import com.vzardd.tmdb.model.SpokenLanguage
import com.vzardd.tmdb.navigation.AppScreens
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.uicomponents.LoadingBox
import com.vzardd.tmdb.util.Constants
import com.vzardd.tmdb.util.TMDBUtil
import com.vzardd.tmdb.viewmodel.MovieViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


@Composable
fun MovieScreen(navController: NavHostController, id: Int?, movieViewModel: MovieViewModel) {

    val dataStore = MovieDataStore(LocalContext.current)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val favState = remember{
        mutableStateOf(false)
    }
    val movieData = movieViewModel.movieDetails
    val creditsData = movieViewModel.movieCredits
    val keywordsData = movieViewModel.movieKeywords
    val recommendationsData = movieViewModel.movieRecommendations
    LaunchedEffect(Unit){
        coroutineScope.launch {
            Log.e("TMDB", "Before is present check")
            favState.value = dataStore.isPresent(id)
            Log.e("TMDB", "After is present check ${favState.value}")
        }
        movieViewModel.getMovieDetails(id!!)
        movieViewModel.getMovieCredits(id)
        movieViewModel.getMovieKeywords(id)
        movieViewModel.getMovieRecommendations(id)
    }

    TheMovieDBTheme {
        Scaffold(topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navController.popBackStack()
                        },imageVector = Icons.Default.ArrowBack, contentDescription = "back", tint = MaterialTheme.colors.onSecondary)
                    Image(modifier = Modifier.size(40.dp),painter = painterResource(id = R.drawable.moviedb_logo), contentDescription = "logo")
                    val icon: Int
                    if(!favState.value){
                        icon = R.drawable.addtofavicon
                    }
                    else{
                        icon = R.drawable.heart
                    }
                    Image(modifier = Modifier
                        .size(30.dp)
                        .padding(2.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            if (favState.value) {
                                coroutineScope.launch {
                                    Log.e("Fav","Before Removed")
                                    dataStore.removeIdFromFav(id?:0, context)
                                    Log.e("Fav","Removed")
                                }
                            } else {
                                coroutineScope.launch {
                                    Log.e("Fav","Before Added")
                                    dataStore.addIdToFav(id?:0, context)
                                    Log.e("Fav","Added")
                                }
                            }
                            Log.d("FavButton", "clicked")
                            favState.value = !favState.value
                        },painter = painterResource(id = icon), contentDescription = "favourites")
                }

            }
        }) {

            if(movieData.value.loading == true){
                LoadingBox()
            }
            else if(movieData.value.e!=null){
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),text = "Something went wrong :(  Check your internet connection and try again!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
            }
            else{
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())) {
                    BackDropBox(poster = movieData.value.data?.poster_path, backdrop = movieData.value.data?.backdrop_path)
                    InfoBox(
                        movieData.value.data?.title?:"Untitled",
                        movieData.value.data?.vote_average?: 0.0,
                        movieData.value.data?.release_date?:" ",
                        movieData.value.data?.runtime?: 0,
                        movieData.value.data?.genres?: emptyList(),
                        movieData.value.data?.tagline?:" ",
                        movieData.value.data?.overview?: "-"
                    )
                    CastsBox(creditsData.value.data?.cast?: emptyList())
                    DetailsBox(
                        movieData.value.data?.status?:"Unknown",
                        movieData.value.data?.original_language?:"English",
                        movieData.value.data?.budget?:0,
                        movieData.value.data?.revenue?:0,
                        movieData.value.data?.spoken_languages?: emptyList(),
                        keywordsData.value.data?.keywords?: emptyList()
                    )
                    RecommendationBox(recommendationsData.value.data?.results?: emptyList(), navController)
                }
            }
        }
    }

}

@Composable
fun RecommendationBox(list: List<Result>, navController: NavController) {
    if(list!=null && list.isNotEmpty()){
        Text( modifier = Modifier.padding(20.dp), text = "You might also like,", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground)
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 0.dp, 20.dp)){
            items(list){
                //on click navigate to movie screen
                RecommendationCard(it.id, it.backdrop_path?:"", it.title?:"Untitled", navController)
            }
        }
    }
}


@Composable
fun RecommendationCard(id: Int, backdrop: String, title: String, navController: NavController) {
    Box(modifier = Modifier.padding(10.dp)){
        Card(modifier = Modifier
            .height(150.dp)
            .width(300.dp)
            .clickable { navController.navigate(AppScreens.MovieScreen.name + "/$id") },elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
            Box(modifier = Modifier.fillMaxSize()){
                if(backdrop.isEmpty()){
                    Image(modifier = Modifier.fillMaxSize(),painter = painterResource(id = R.drawable.default_poster), contentDescription = "backdrop", contentScale = ContentScale.Crop)
                }
                else{
                    Image(modifier = Modifier.fillMaxSize(),painter = rememberAsyncImagePainter(model = Constants.IMAGE_BASE_URL+backdrop, placeholder = painterResource(
                        id = R.drawable.default_poster
                    )), contentDescription = "backdrop", contentScale = ContentScale.Crop)
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black
                                )
                            )
                        )
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = title, color = MaterialTheme.colors.onPrimary, fontSize = 18.sp, fontWeight = FontWeight(350))
                }
            }
        }
    }
}

@Composable
fun DetailsBox(status: String, language: String, budget: Int, revenue: Int, langList: List<SpokenLanguage>, keywords: List<Keyword>) {
    //status
    DetailsRow(key = "Status", value = status)
    //original language
    DetailsRow(key = "Original Language", value = TMDBUtil.getLanguage(language,langList))
    //budget
    DetailsRow(key = "Budget", value = TMDBUtil.moneyFormatter(budget) )
    //revenue
    DetailsRow(key = "Revenue", value = TMDBUtil.moneyFormatter(revenue) )
    //key words
    Text( modifier = Modifier.padding(25.dp,20.dp,0.dp,0.dp), text = "Keywords", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    KeywordsBox(keywords)
    Divider()
}

@Composable
fun KeywordsBox(keywords: List<Keyword>) {
    FlowRow(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)){
        for(x in keywords){
            Chip(value = x.name)
        }
    }
}

@Composable
fun Chip(value: String){
    Box(modifier = Modifier.padding(5.dp)){
        Card(backgroundColor = MaterialTheme.colors.onSecondary,shape = RoundedCornerShape(5.dp), elevation = 2.dp) {
            Text(modifier = Modifier
                .padding(5.dp),text = value, color = MaterialTheme.colors.onBackground)
        }
    }
}

@Composable
fun DetailsRow(key: String, value: String){
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, 15.dp)) {
        Text(text = key, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground)
        Text(text = value, fontSize = 18.sp, color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun CastsBox(list: List<Cast>) {
    if(list.isNotEmpty()){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 0.dp, 20.dp)) {
            Text(text = "Top Billed Cast", color = MaterialTheme.colors.onBackground, fontSize = 22.sp, fontWeight = FontWeight(550) ,fontFamily = FontFamily.SansSerif)
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp)){
                items(list.sortedBy { it.order }){
                    ProfileCard(it.profile_path?:"", it.name?:"No Name", it.character?:" ")
                    Spacer(modifier = Modifier.width(15.dp))
                }
            }
        }
        Divider()
    }
}

@Composable
fun ProfileCard(profile_img: String, name: String, character: String) {
    Card(modifier = Modifier.defaultMinSize(100.dp,300.dp),elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
        Column(
            Modifier
                .width(150.dp)) {
            if(profile_img.isEmpty()){
                Image(modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),painter = painterResource(id = R.drawable.default_cast), contentDescription = "cast", contentScale = ContentScale.Crop)
            }
            else{
                Image(modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),painter = rememberAsyncImagePainter(model = Constants.IMAGE_BASE_URL+profile_img, placeholder = painterResource(
                    id = R.drawable.default_cast
                )), contentDescription = "cast", contentScale = ContentScale.Crop)
            }
            Text(modifier = Modifier.padding(10.dp),text = name, fontSize = 18.sp, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, lineHeight = 25.sp)
            Text(modifier = Modifier.padding(10.dp,0.dp,10.dp,10.dp),text = character, fontSize = 16.sp, color = MaterialTheme.colors.onBackground, lineHeight = 25.sp)
        }
    }
}

@Composable
fun InfoBox(movieTitle: String, voteAvg: Double, releaseDate: String, runtime: Int, genres: List<Genre>, tagline: String, overview: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = movieTitle, color = MaterialTheme.colors.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "(${TMDBUtil.getYear(releaseDate)})", color = MaterialTheme.colors.onSecondary, fontSize = 18.sp)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "${(voteAvg*10).toInt()}%", color = MaterialTheme.colors.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Image(modifier = Modifier.size(22.dp),painter = painterResource(id = R.drawable.heart), contentDescription = "loves")
            }
            Divider(modifier = Modifier
                .height(20.dp)
                .width(1.dp), color = MaterialTheme.colors.onSecondary)
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "play", tint = MaterialTheme.colors.onSecondary)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Play Trailer", color = MaterialTheme.colors.onSecondary, fontSize = 18.sp)
            }
        }
        Divider(color = MaterialTheme.colors.onBackground, modifier = Modifier.height(1.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.deep_blue))
                .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            //must change
            Text(text = "${releaseDate.replace('-','/')} • ${TMDBUtil.getRunTime(runtime)}", color = MaterialTheme.colors.onPrimary, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = TMDBUtil.getGenres(genres), color = MaterialTheme.colors.onPrimary, fontSize = 18.sp)
        }
        Divider(color = MaterialTheme.colors.onBackground, modifier = Modifier.height(1.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(20.dp)) {
            //must change - tagline
            Text(text = tagline, color = MaterialTheme.colors.onSecondary, fontSize = 18.sp, fontStyle = FontStyle.Italic)
            Text(modifier = Modifier.padding(0.dp,10.dp),text = "Overview", color = MaterialTheme.colors.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            //must change - overview
            Text(text = overview, fontSize = 16.sp, color = MaterialTheme.colors.onPrimary, lineHeight = 25.sp)
        }
    }
}

@Composable
fun BackDropBox(poster: String? = "", backdrop: String? = ""){
    Box(
        Modifier
            .fillMaxWidth()
            .height(250.dp)){
        if(backdrop == null || backdrop.isEmpty()){
            Image(
                painter = painterResource(id = R.drawable.default_poster),
                contentDescription = "backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        else{
            Image(
                painter = rememberAsyncImagePainter(model = Constants.IMAGE_BASE_URL + backdrop, placeholder = painterResource(
                    id = R.drawable.default_poster
                )),
                contentDescription = "backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            Color.Transparent
                        )
                    )
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Bottom
        ){
            PosterCard(poster)
        }
    }
}


@Composable
fun PosterCard(poster: String? = "") {
    Card(
        Modifier
            .height(150.dp)
            .width(100.dp), elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
        if(poster == null || poster.isEmpty()){
            Image(painter = painterResource(id = R.drawable.default_poster), contentDescription = "poster", contentScale = ContentScale.Crop)
        }
        else{
            Image(painter = rememberAsyncImagePainter(model = Constants.IMAGE_BASE_URL+poster, placeholder = painterResource(
                id = R.drawable.default_poster
            )), contentDescription = "poster", contentScale = ContentScale.Crop)
        }

    }
}
