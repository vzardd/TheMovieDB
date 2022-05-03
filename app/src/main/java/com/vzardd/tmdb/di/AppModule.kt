package com.vzardd.tmdb.di

import com.vzardd.tmdb.network.ApiService
import com.vzardd.tmdb.repository.MovieRepo
import com.vzardd.tmdb.repository.SearchRepo
import com.vzardd.tmdb.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSearchRepo(apiService: ApiService) = SearchRepo(apiService)

    @Singleton
    @Provides
    fun provideMovieRepo(apiService: ApiService) = MovieRepo(apiService)

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