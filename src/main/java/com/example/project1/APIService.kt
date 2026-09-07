package com.example.project1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/movie")
    fun searchMovies(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "credits"
    ): Call<MovieDetails>
}