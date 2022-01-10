package com.anietie.moviezone.di

import android.content.Context
import com.anietie.moviezone.data.repo.MovieRepository
import com.anietie.moviezone.data.local.MoviesDatabase
import com.anietie.moviezone.data.local.MoviesLocalDataSource
import com.anietie.moviezone.data.remote.MoviesRemoteDataSource
import com.anietie.moviezone.data.remote.api.ApiClient.getInstance
import com.anietie.moviezone.utils.AppExecutors

/**
 * Class that handles object creation and dependency injection.
 *
 */
object Injection {
    /**
     * Creates an instance of MoviesRemoteDataSource
     */
    fun provideMoviesRemoteDataSource(): MoviesRemoteDataSource {
        val apiService = getInstance()
        val executors: AppExecutors = AppExecutors.instance
        return MoviesRemoteDataSource.getInstance(apiService, executors)
    }

    /**
     * Creates an instance of MoviesRemoteDataSource
     */
    fun provideMoviesLocalDataSource(context: Context): MoviesLocalDataSource {
        val database = MoviesDatabase.getInstance(context.applicationContext)
        return MoviesLocalDataSource.getInstance(database!!)
    }

    /**
     * Creates an instance of MovieRepository
     */
    fun provideMovieRepository(context: Context): MovieRepository {
        val remoteDataSource = provideMoviesRemoteDataSource()
        val localDataSource = provideMoviesLocalDataSource(context)
        val executors: AppExecutors = AppExecutors.instance
        return MovieRepository.getInstance(
            localDataSource,
            remoteDataSource,
            executors
        )
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val repository = provideMovieRepository(context)
        return ViewModelFactory.getInstance(repository)
    }
}
