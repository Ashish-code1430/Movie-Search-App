package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    private val token = ApiConfig.TMDB_TOKEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.etSearch)
        searchButton = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)

        movieAdapter = MovieAdapter(emptyList()) { movie ->

            val intent = Intent(
                this,
                MovieDetailActivity::class.java
            )

            intent.putExtra("MOVIE_ID", movie.id)

            startActivity(intent)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = movieAdapter

        searchButton.setOnClickListener {

            val query = searchEditText.text.toString().trim()

            if (query.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter a movie name",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                searchMovies(query)
            }
        }
    }

    private fun searchMovies(query: String) {

        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.searchMovies(
            "Bearer $token",
            query
        ).enqueue(object : Callback<MovieResponse> {

            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {

                progressBar.visibility = View.GONE

                if (response.isSuccessful) {

                    val movies =
                        response.body()?.results ?: emptyList()

                    movieAdapter.updateMovies(movies)

                    if (movies.isEmpty()) {

                        Toast.makeText(
                            this@MainActivity,
                            "No movies found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "API Error: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<MovieResponse>,
                t: Throwable
            ) {

                progressBar.visibility = View.GONE

                Toast.makeText(
                    this@MainActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}