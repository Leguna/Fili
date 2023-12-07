package com.arksana.fili.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val movieListAdapter = MoviePagingAdapter { _ ->
        }
        recyclerView.adapter = movieListAdapter

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