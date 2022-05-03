package com.vzardd.tmdb.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vzardd.tmdb.R
import com.vzardd.tmdb.util.Constants
import com.vzardd.tmdb.util.TMDBUtil
import kotlin.math.min


@Composable
fun MovieRowCard(id: Int, poster: String = "", movieTitle: String = "Untitled", releaseDate: String = " ", overview: String = "Info unavailable.", onClick: (Int) -> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(10.dp)){
        Card(modifier = Modifier.clickable { onClick(id) },elevation = 5.dp, shape = RoundedCornerShape(10.dp)) {
            Row(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if(poster == null || poster.isEmpty()){
                    Image(modifier = Modifier.weight(1f),painter = painterResource(id = R.drawable.default_poster), contentDescription = "poster", contentScale = ContentScale.Crop)
                }
                else{
                    Image(modifier = Modifier.weight(1f),painter = rememberAsyncImagePainter(model = Constants.IMAGE_BASE_URL+poster), contentDescription = "poster", contentScale = ContentScale.Crop)
                }
                Column(
                    Modifier
                        .weight(2f)
                        .padding(10.dp), verticalArrangement = Arrangement.Center) {

                    Text(text = movieTitle, color = MaterialTheme.colors.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = TMDBUtil.dateFormatter(releaseDate), color = MaterialTheme.colors.onSecondary, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    if(overview.length<50){
                        Text(text = overview, color = MaterialTheme.colors.onBackground, fontSize = 18.sp)
                    }
                    else{
                        Text(text = overview.substring(0,49)+"...", color = MaterialTheme.colors.onBackground, fontSize = 18.sp)
                    }

                }
            }
            
        }
    }
}