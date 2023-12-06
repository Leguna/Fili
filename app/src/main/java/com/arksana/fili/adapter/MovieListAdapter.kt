package com.arksana.fili.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arksana.fili.databinding.ItemMovieBinding
import com.arksana.fili.model.MovieList.*
import com.bumptech.glide.Glide

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    private val movieList = mutableListOf<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(movieList: List<Movie>) {
        this.movieList.clear()
        this.movieList.addAll(movieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            with(binding) {
                Glide.with(itemView.context)
                    .load("https://www.themoviedb.org/t/p/w138_and_h175_face${movie.posterPath}")
                    .into(ivMoviePoster)
                tvMovieTitle.text = movie.title
                tvMovieDescription.text = movie.overview
                tvMovieYear.text = movie.releaseDate
                val percent =
                    ("${movie.voteAverage}".substring(0, 2).toFloat() * 10).toInt().toString() + "%"
                tvMovieUserRatingValue.text = percent
                // Add thousand separator
                var formattedUserRatingThousand = movie.voteCount
                tvMovieUserRating.text = formattedUserRatingThousand.toString()
                    .replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1,") + " users"
                pbMovie.progress = "${movie.voteAverage}".toFloat().toInt()
                pbMovie.max = 10
            }
        }
    }

}