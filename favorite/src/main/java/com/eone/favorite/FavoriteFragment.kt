package com.eone.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eone.favorite.databinding.FragmentFavoriteBinding
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteFragment : Fragment() {
    private var _favoriteFragmentBinding : FragmentFavoriteBinding? = null
    private val binding get() =  requireNotNull(_favoriteFragmentBinding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _favoriteFragmentBinding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView(){
        binding.apply {
            vpFavorite.adapter = FavoritePagerAdapter(childFragmentManager,viewLifecycleOwner.lifecycle)
            TabLayoutMediator(tlFavorite, vpFavorite) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpFavorite.adapter = null
        _favoriteFragmentBinding = null
    }

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.movies, R.string.tvshows)
    }
}