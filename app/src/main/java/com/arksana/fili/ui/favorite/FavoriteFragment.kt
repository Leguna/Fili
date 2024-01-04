package com.arksana.fili.ui.favorite

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingData
import com.arksana.fili.R
import com.arksana.fili.adapter.MoviePagingAdapter
import com.arksana.fili.databinding.FragmentFavoriteBinding
import com.arksana.fili.ui.main.MainFragmentDirections
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        postponeEnterTransition()
        val animation = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        binding.rvFavorite.adapter = MoviePagingAdapter({ movie, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to movie.id.toString(),
            )
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action =
                MainFragmentDirections.actionMainFragmentToNavigationDetail(
                    Gson().toJson(movie)
                )
            navController.navigate(action, extras)

        }, emptyView = binding.emptyNotification.root)

        viewModel.allMovies.observe(viewLifecycleOwner) {
            (binding.rvFavorite.adapter as MoviePagingAdapter).submitData(
                lifecycle,
                PagingData.from(it)
            )
            if (it.isEmpty()) {
                binding.emptyNotification.root.visibility = View.VISIBLE
            } else {
                binding.emptyNotification.root.visibility = View.GONE
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}