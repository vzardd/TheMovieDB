package com.vzardd.tmdb.di

import android.content.Context
import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import androidx.room.TypeConverter
import com.vzardd.tmdb.network.ApiService
import com.vzardd.tmdb.repository.MovieRepo
import com.vzardd.tmdb.repository.SearchRepo
import com.vzardd.tmdb.room.MovieDao
import com.vzardd.tmdb.room.MovieDatabase
import com.vzardd.tmdb.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSearchRepo(apiService: ApiService) = SearchRepo(apiService)

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) : MovieDatabase{
        return Room.databaseBuilder(context,MovieDatabase::class.java,"movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideMovieRepo(apiService: ApiService, movieDao: MovieDao, @ApplicationContext context: Context): MovieRepo {
        return MovieRepo(apiService, movieDao, context)
    }



    @Singleton
    @Provides
    fun provideMoviesDao(movieDatabase: MovieDatabase): MovieDao{
        return movieDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideApiService() : ApiService{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}