package com.vzardd.tmdb.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.vzardd.tmdb.model.MoviesList
import com.vzardd.tmdb.model.Result
import com.vzardd.tmdb.navigation.AppScreens
import com.vzardd.tmdb.ui.theme.TheMovieDBTheme
import com.vzardd.tmdb.uicomponents.LoadingBox
import com.vzardd.tmdb.uicomponents.MovieRowCard
import com.vzardd.tmdb.viewmodel.SearchViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavHostController, searchViewModel: SearchViewModel) {
    val searchField = remember{
        mutableStateOf("")
    }

    val localManager =  LocalFocusManager.current

    val searchList = searchViewModel.searchList
    TheMovieDBTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            SearchBox(searchField.value, searchViewModel, fetchList = {

                Log.d("InFetchList","called")

                searchViewModel.getSearchMoviesList(searchField.value)

            }){
                searchField.value = it
            }
            if(searchList.value.loading == true){
                LoadingBox()
            }
            else if(searchList.value.data.isNullOrEmpty() && searchList.value.e==null){
                if(searchList.value.data == null || searchList.value.data?.toString() == null){
                    Text(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),text = "Search from millions of movies online!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
                }
                else{
                    Text(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),text = "No results found!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
                }

                Log.d("MySize1","sorry")
            }
            else if(searchList.value.e!=null){
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),text = "Something went wrong :(  Check your internet connection and try again!", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center, fontSize = 18.sp)
            }
            else if(searchList.value.data != null){
                Log.d("MySize",""+searchList.value.data!!.size)
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)){
                    itemsIndexed(searchList.value.data!!){
                        index,it ->
                        //on click navigate to movie screen

                        MovieRowCard(id = it.id, movieTitle = it.title, overview = it.overview, poster = it.poster_path, releaseDate = it.release_date){
                            localManager.clearFocus(true)
                            navController.navigate(AppScreens.MovieScreen.name+"/${it}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBox(fieldValue: String, searchViewModel: SearchViewModel,fetchList: () -> Unit,updateField: (String) -> Unit) {
    val focusRequester = FocusRequester()

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
        TextField(modifier = Modifier
            .padding(10.dp, 20.dp)
            .focusRequester(focusRequester = focusRequester),placeholder = {
            Text(text = "Search...", color = MaterialTheme.colors.onSecondary, fontSize = 18.sp)
        },value = fieldValue, onValueChange = {
            updateField(it)
        }, shape = CircleShape, singleLine = true, leadingIcon = {
            Icon(modifier = Modifier.size(30.dp),imageVector = Icons.Default.Search, contentDescription = "search")
        }, colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ))
        Card(modifier = Modifier.clickable {
             if(fieldValue.isNotEmpty())  {
                 fetchList()
             }
        },shape = CircleShape, elevation = 2.dp, backgroundColor = MaterialTheme.colors.primary) {
            Icon(modifier = Modifier
                .padding(10.dp)
                .size(30.dp),imageVector = Icons.Default.Search, contentDescription = "search", tint = Color.White)
        }
    }

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
}
