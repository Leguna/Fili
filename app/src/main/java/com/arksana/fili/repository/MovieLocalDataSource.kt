package com.arksana.fili.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.arksana.fili.MyApp
import com.arksana.fili.model.Movie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDatabaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieLocalDataSource {
        return Room.databaseBuilder(
            context,
            MovieLocalDataSource::class.java,
            "movie_database"
        ).build()
    }
    @Provides
    fun provideMovieDao(movieDatabase: MovieLocalDataSource): MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MoviesRemoteDataSource,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepository(remoteDataSource, movieDao)
    }
}


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE is_favorite = 1")
    fun getAllFavoriteMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)
}

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieLocalDataSource : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
