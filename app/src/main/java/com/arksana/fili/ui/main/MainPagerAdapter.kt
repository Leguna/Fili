package com.arksana.fili.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arksana.fili.ui.favorite.FavoriteFragment
import com.arksana.fili.ui.home.HomeFragment
import com.arksana.fili.ui.settings.SettingsFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments: ArrayList<Fragment> = ArrayList(NUM_PAGES)

    init {
        fragments.add(HomeFragment())
        fragments.add(FavoriteFragment())
        fragments.add(SettingsFragment())
    }


    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }

            1 -> FavoriteFragment()
            2 -> SettingsFragment()
            else -> fragments[position]
        }
    }


    companion object {
        private const val NUM_PAGES = 3
    }

}
