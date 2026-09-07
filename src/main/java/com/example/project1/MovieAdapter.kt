package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster: ImageView = itemView.findViewById(R.id.ivPoster)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
        val releaseDate: TextView = itemView.findViewById(R.id.tvReleaseDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val movie = movies[position]

        holder.title.text = movie.title ?: "Unknown Title"

        holder.rating.text =
            "⭐ ${movie.vote_average ?: 0.0}"

        holder.releaseDate.text =
            "Release: ${movie.release_date ?: "Unknown"}"

        if (movie.poster_path != null) {

            val imageUrl =
                "https://image.tmdb.org/t/p/w500${movie.poster_path}"

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.poster)

        } else {

            holder.poster.setImageResource(
                android.R.drawable.ic_menu_report_image
            )
        }

        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}