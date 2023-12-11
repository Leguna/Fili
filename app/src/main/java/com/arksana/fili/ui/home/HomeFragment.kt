package com.arksana.fili.ui.home

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arksana.fili.R
import com.arksana.fili.adapter.MoviePagingAdapter
import com.arksana.fili.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var adapter: MoviePagingAdapter? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.rvMovies
        val searchView = binding.searchView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val movieListAdapter = MoviePagingAdapter { movie, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to "movie_poster_transition"
            )
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(movie.toString())
            // TODO: Add transition to detail fragment using extras
            navController.navigate(action)
        }
        recyclerView.adapter = movieListAdapter


        binding.searchView.cardView.transitionName = "home_search_transition"

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)


        searchView.etSearch.setOnClickListener {
            searchView.etSearch.isCursorVisible = true
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            val extras = FragmentNavigatorExtras(searchView.cardView to "home_search_transition")
            navController.navigate(
                R.id.action_navigation_main_to_navigation_search,
                null,
                null,
                extras
            )
        }

        homeViewModel.getPopularMovie().observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                movieListAdapter.submitData(pagingData)
            }
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}