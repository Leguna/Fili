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
import com.arksana.fili.ui.main.MainFragmentDirections
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: MoviePagingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        postponeEnterTransition()
        val animation =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        val recyclerView = binding.rvMovies
        val emptyView = layoutInflater.inflate(R.layout.empty_notification, recyclerView, false);
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val movieListAdapter = MoviePagingAdapter({ movie, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to movie.id.toString(),
            )
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action =
                MainFragmentDirections.actionMainFragmentToNavigationDetail(Gson().toJson(movie))
            navController.navigate(action, extras)
        }, emptyView)
        recyclerView.adapter = movieListAdapter
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }

        val searchView = binding.searchView
        searchView.etSearch.setOnClickListener {
            searchView.etSearch.isCursorVisible = true
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            val extras = FragmentNavigatorExtras(
                searchView.cardView to "search_card_view",
            )

            navController.navigate(
                MainFragmentDirections.actionMainFragmentToNavigationSearch(),
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}