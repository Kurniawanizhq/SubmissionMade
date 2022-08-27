package com.eone.favorite

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eone.favorite.movies.FavoriteMovieFragment
import com.eone.favorite.tvshows.FavoriteTvShowFragment


class FavoritePagerAdapter(fm: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FavoriteMovieFragment()
            1 -> fragment = FavoriteTvShowFragment()
        }
        return fragment as Fragment
    }
}