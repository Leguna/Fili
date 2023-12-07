package com.arksana.fili.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arksana.fili.BuildConfig
import com.arksana.fili.databinding.ItemMovieBinding
import com.arksana.fili.model.Movie
import com.arksana.fili.model.MovieList.*
import com.arksana.fili.utilities.GlideShimmerLoader

class MoviePagingAdapter(private val movieClickListener: MovieClickListener) :
    PagingDataAdapter<Movie, MovieViewHolder>(MovieDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!!, movieClickListener)
    }
}

class MovieViewHolder(private val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(movie: Movie, movieClickListener: MovieClickListener) {
        with(binding) {
            itemView.setOnClickListener {
                movieClickListener.onMovieClick(movie)
            }
            GlideShimmerLoader.GlideShimmerLoader.loadWithShimmer(
                itemView.context,
                "${BuildConfig.IMAGE_BASE_URL}${movie.posterPath}",
                ivMoviePoster
            )
            tvMovieTitle.text = movie.title
            tvMovieDescription.text = movie.overview
            tvMovieYear.text = movie.releaseDate
            val percent =
                ("${movie.voteAverage}".substring(0, 2).toFloat() * 10).toInt().toString() + "%"
            tvMovieUserRatingValue.text = percent
            val formattedUserRatingThousand = movie.voteCount
            tvMovieUserRating.text = formattedUserRatingThousand.toString()
                .replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1,") + " users"
            pbMovie.progress = "${movie.voteAverage}".toFloat().toInt()
            pbMovie.max = 10
        }
    }
}

class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}

fun interface MovieClickListener {
    fun onMovieClick(movie: Movie)
}