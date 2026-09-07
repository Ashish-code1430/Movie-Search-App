package com.example.project1

data class MovieDetails(
    val id: Int,
    val title: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val overview: String?,
    val release_date: String?,
    val runtime: Int?,
    val budget: Long?,
    val revenue: Long?,
    val vote_average: Double?,
    val vote_count: Int?,
    val imdb_id: String?,
    val genres: List<Genre>?,
    val credits: Credits?
)

data class Genre(
    val id: Int,
    val name: String
)

data class Credits(
    val cast: List<Cast>?,
    val crew: List<Crew>?
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String?,
    val profile_path: String?
)

data class Crew(
    val id: Int,
    val name: String,
    val job: String?,
    val department: String?
)