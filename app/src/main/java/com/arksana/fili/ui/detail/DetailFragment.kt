package com.arksana.fili.ui.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arksana.fili.R
import com.arksana.fili.databinding.FragmentDetailBinding
import com.arksana.fili.model.Movie
import com.arksana.fili.utilities.GlideShimmerLoader
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment @Inject constructor() : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.movie.observe(viewLifecycleOwner, ::setMovieView)
        viewModel.allMovies.observe(viewLifecycleOwner) { movies ->
            val currentMovie = movies.find { it.id == viewModel.movie.value?.id }
            val isFavorite = currentMovie?.isFavorite ?: false
            viewModel.setIsFavorite(isFavorite)
            updateFavoriteIconState(isFavorite)
        }
        viewModel.setMovie(DetailFragmentArgs.fromBundle(requireArguments()).movie)
        viewModel.movie.observe(viewLifecycleOwner) {
            binding.imageView.transitionName = it.id.toString()
        }

        val animation = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)

        val toolbar = binding.toolbar
        toolbar.setTitleTextAppearance(
            requireContext(),
            android.R.style.TextAppearance_Material_Widget_ActionBar_Title
        )
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return binding.root
    }

    private fun setMovieView(movie: Movie) {
        binding.textView.text = movie.overview
        binding.toolbar.title = movie.title
        GlideShimmerLoader.GlideShimmerLoader.loadWithShimmer(
            requireContext(),
            "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            binding.imageView
        )
        binding.favoriteIcon.setOnClickListener {
            viewModel.favoriteIconClicked()
        }
        updateFavoriteIconState(movie.isFavorite)
    }

    private fun updateFavoriteIconState(isFavorite: Boolean) {
        val favoriteIcon = binding.favoriteIcon
        if (isFavorite) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite)
            favoriteIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border)
            favoriteIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }
}