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
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arksana.fili.R
import com.arksana.fili.adapter.MoviePagingAdapter
import com.arksana.fili.databinding.FragmentSearchBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieListAdapter: MoviePagingAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setupSharedElementTransition()
        setupSearch()
        setupAdapter()


        viewModel.query.observe(viewLifecycleOwner) {
            viewModel.searchMovie(it).observe(viewLifecycleOwner) {
                movieListAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun setupSharedElementTransition() {
        postponeEnterTransition()
        val animation =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun setupAdapter() {
        val recyclerView = binding.rvSearch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        movieListAdapter = MoviePagingAdapter({ movie, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to movie.id.toString(),
            )
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action =
                SearchFragmentDirections.actionNavigationSearchToNavigationDetail(
                    Gson().toJson(
                        movie
                    )
                )
            navController.navigate(action, extras)
        }, emptyView = binding.emptyNotification.root)
        recyclerView.adapter = movieListAdapter

        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupSearch() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        val searchView = binding.searchView
        searchView.etSearch.isFocusableInTouchMode = true
        searchView.etSearch.isCursorVisible = true
        searchView.etSearch.postDelayed({
            searchView.etSearch.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchView.etSearch, 0)
        }, 100)

        searchView.etSearch.setOnEditorActionListener { _, _, _ ->
            val query = searchView.etSearch.text.toString()
            viewModel.query.value = query
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchView.etSearch.windowToken, 0)
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}