package com.vzardd.tmdb.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
import com.google.gson.Gson
import com.vzardd.tmdb.datastore.FavList
import com.vzardd.tmdb.datastore.MovieCache
import com.vzardd.tmdb.model.Genre
import com.vzardd.tmdb.model.SpokenLanguage
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

object TMDBUtil {
    fun dateFormatter(str: String): String{
        if(str==null || str.length<2){
            return " "
        }
        val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(str , apiFormat)
        val sDate = (date.month.toString().lowercase().substring(0,3) + " " + date.dayOfMonth.toString() + ", " + date.year.toString())
        return sDate.replaceFirstChar{
            it.uppercase()
        }
    }

    fun moneyFormatter(value: Int) : String{
        if(value==0){
            return "-"
        }

        val numberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.setMaximumFractionDigits(0);
        val convert = numberFormat.format(value)
        return convert
    }

    fun getYear(releaseDate: String): String {
        if(releaseDate == null || releaseDate.length < 2){
            return " "
        }
        else{
            return releaseDate.substring(0,4)
        }
    }

    fun getRunTime(runtime: Int): String{
        val h = runtime/60
        val m = runtime%60
        return "${h}h ${m}m"
    }

    fun getGenres(genres: List<Genre>): String {
        if(genres == null || genres.size == 0){
            return " "
        }
        var s = ""
        for(genre in genres){
            s += ", " + genre.name
        }
        return s.substring(2)
    }

    fun getLanguage(key: String, list: List<SpokenLanguage>): String{
        if(key==null || key.isEmpty() || list == null || list.isEmpty())
            return "-"
        for(value in list){
            if(value.iso_639_1 == key)
                return value.name
        }
        return "-"
    }

    fun jsonToObject(json: String): FavList{
        if(json.isEmpty()){
            return FavList()
        }
        return Gson().fromJson(json,FavList::class.java)
    }

    fun objectToJson(obj: FavList): String{
        return Gson().toJson(obj)
    }
}