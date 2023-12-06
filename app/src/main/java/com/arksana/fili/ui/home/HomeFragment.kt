package com.arksana.fili.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arksana.fili.adapter.MovieListAdapter
import com.arksana.fili.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        val adapter = MovieListAdapter()
        recyclerView.adapter = adapter
        homeViewModel.movieList.observe(viewLifecycleOwner) {
            adapter.setMovieList(it ?: listOf())
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshMovieList()
            swipeRefreshLayout.isRefreshing = false
        }

        homeViewModel.loadMovies()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}