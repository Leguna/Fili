package com.arksana.fili

import android.content.Context
import com.arksana.fili.repository.MovieApiService
import com.arksana.fili.repository.MovieDao
import com.arksana.fili.repository.MovieLocalDataSource
import com.arksana.fili.repository.MovieRepository
import com.arksana.fili.repository.MoviesRemoteDataSource
import com.arksana.fili.repository.MoviesRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    fun provideMoviesRemoteDataSource(movieService: MovieApiService): MoviesRemoteDataSource {
        return MoviesRemoteDataSourceImpl(movieService)
    }

}