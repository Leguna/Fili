package com.arksana.fili.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arksana.fili.R
import com.arksana.fili.adapter.MoviePagingAdapter
import com.arksana.fili.databinding.FragmentSearchBinding
import com.arksana.fili.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.searchView.cardView.transitionName = "home_search_transition"
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        val searchView = binding.searchView
        searchView.etSearch.isFocusableInTouchMode = true
        searchView.etSearch.isCursorVisible = true

        val recyclerView = binding.rvSearch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val movieListAdapter = MoviePagingAdapter { movie, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to "movie_poster_transition"
            )
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action =
                HomeFragmentDirections.actionNavigationHomeToNavigationDetail(movie.toString())
            // TODO: Add transition to detail fragment using extras
            navController.navigate(action)
        }
        recyclerView.adapter = movieListAdapter
        searchView.etSearch.postDelayed({
            searchView.etSearch.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchView.etSearch, 0)
        }, 100)

        searchView.etSearch.setOnEditorActionListener { _, _, _ ->
            val query = searchView.etSearch.text.toString()
            searchViewModel.searchMovie(query).observe(viewLifecycleOwner) {
                movieListAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchView.etSearch.windowToken, 0)

            false
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}