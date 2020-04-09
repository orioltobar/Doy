package com.napptilians.domain.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {

    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>

    fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>>

    suspend fun login(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun register(email:String, password:String): Response<AuthResult, ErrorModel>
}