package com.eone.submissionmade.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.eone.submissionmade.R
import com.eone.submissionmade.databinding.ActivityMainBinding
import com.eone.submissionmade.movies.MoviesFragment
import com.eone.submissionmade.tvshows.TvShowsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationChange(MoviesFragment())
        setupView()
    }

    private fun setupView() {
        binding.apply {
            bottomNav.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_movies -> navigationChange(MoviesFragment())
                    R.id.nav_tvshows -> navigationChange(TvShowsFragment())
                    R.id.nav_favorite -> navToFavorite()
                }
                true
            }

            toolbar.title = "THE MOVIEDB"
            toolbar.elevation = 0f
            toolbar.setTitleTextAppearance(this@MainActivity,R.style.ScreamRealTextAppearance)

        }
    }

    private val className: String
        get() = "com.eone.favorite.FavoriteFragment"

    private fun navToFavorite() {
        val fragment = instantiateFragment(className)
        if (fragment != null) {
            navigationChange(fragment)
        }
    }

    private fun instantiateFragment(className: String): Fragment? {
        return try {
            Class.forName(className).newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun navigationChange(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}