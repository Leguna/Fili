package com.arksana.fili.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.arksana.fili.R
import com.arksana.fili.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val viewPager = binding.viewPager
        val bottomNavigationView = binding.bottomNavView
        val navHostFragment = findNavController()
        val adapter = MainPagerAdapter(requireActivity())
        viewPager.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
                when (position) {
                    0 -> navHostFragment.popBackStack(R.id.navigation_home, true)
                    1 -> navHostFragment.popBackStack(R.id.navigation_favorite, true)
                    2 -> navHostFragment.popBackStack(R.id.navigation_settings, true)
                }
            }
        })
        viewPager.adapter = adapter

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewPager.currentItem = 0
                    true
                }

                R.id.navigation_favorite -> {
                    viewPager.currentItem = 1
                    true
                }

                R.id.navigation_settings -> {
                    viewPager.currentItem = 2
                    true
                }

                else -> false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}