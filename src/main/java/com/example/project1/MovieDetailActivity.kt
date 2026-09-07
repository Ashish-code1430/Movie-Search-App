package com.example.project1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: TextView
    private lateinit var releaseDate: TextView
    private lateinit var runtime: TextView
    private lateinit var budget: TextView
    private lateinit var revenue: TextView
    private lateinit var genres: TextView
    private lateinit var director: TextView
    private lateinit var cast: TextView
    private lateinit var overview: TextView
    private lateinit var imdb: TextView

    private val token = ApiConfig.TMDB_TOKEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movie_detail)

        poster = findViewById(R.id.ivDetailPoster)
        title = findViewById(R.id.tvDetailTitle)
        rating = findViewById(R.id.tvRating)
        releaseDate = findViewById(R.id.tvReleaseDate)
        runtime = findViewById(R.id.tvRuntime)
        budget = findViewById(R.id.tvBudget)
        revenue = findViewById(R.id.tvRevenue)
        genres = findViewById(R.id.tvGenres)
        director = findViewById(R.id.tvDirector)
        cast = findViewById(R.id.tvCast)
        overview = findViewById(R.id.tvOverview)
        imdb = findViewById(R.id.tvImdb)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)

        if (movieId != -1) {
            getMovieDetails(movieId)
        } else {
            Toast.makeText(
                this,
                "Movie not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun getMovieDetails(movieId: Int) {

        RetrofitClient.api.getMovieDetails(
            "Bearer $token",
            movieId
        ).enqueue(object : Callback<MovieDetails> {

            override fun onResponse(
                call: Call<MovieDetails>,
                response: Response<MovieDetails>
            ) {

                if (response.isSuccessful) {

                    val movie = response.body()

                    if (movie != null) {
                        displayMovieDetails(movie)
                    }

                } else {

                    Toast.makeText(
                        this@MovieDetailActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<MovieDetails>,
                t: Throwable
            ) {

                Toast.makeText(
                    this@MovieDetailActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun displayMovieDetails(movie: MovieDetails) {

        title.text = movie.title ?: "Unknown"

        rating.text =
            "⭐ TMDB Rating: ${movie.vote_average ?: 0.0}"

        releaseDate.text =
            "📅 Release Date: ${movie.release_date ?: "Unknown"}"

        runtime.text =
            "⏱ Runtime: ${movie.runtime ?: 0} minutes"

        budget.text =
            "💰 Budget: ${formatMoney(movie.budget)}"

        revenue.text =
            "💵 Revenue: ${formatMoney(movie.revenue)}"

        genres.text =
            "🎞 Genres: ${
                movie.genres?.joinToString(", ") { it.name }
                    ?: "Unknown"
            }"

        val directorName = movie.credits?.crew
            ?.firstOrNull {
                it.job.equals("Director", ignoreCase = true)
            }
            ?.name

        director.text =
            "🎬 Director: ${directorName ?: "Unknown"}"

        val castNames = movie.credits?.cast
            ?.take(10)
            ?.joinToString("\n") {
                "• ${it.name} as ${it.character ?: "Unknown"}"
            }

        cast.text =
            "🎭 Cast:\n${castNames ?: "Not available"}"

        overview.text =
            "📝 Overview:\n${movie.overview ?: "No overview available"}"

        if (!movie.imdb_id.isNullOrEmpty()) {

            imdb.text =
                "🔗 IMDb: https://www.imdb.com/title/${movie.imdb_id}/"

        } else {

            imdb.text = "🔗 IMDb: Not available"
        }

        if (!movie.poster_path.isNullOrEmpty()) {

            val imageUrl =
                "https://image.tmdb.org/t/p/w500${movie.poster_path}"

            Glide.with(this)
                .load(imageUrl)
                .into(poster)
        }
    }

    private fun formatMoney(amount: Long?): String {

        if (amount == null || amount == 0L) {
            return "Not available"
        }

        return "$" + String.format(
            "%,d",
            amount
        )
    }
}