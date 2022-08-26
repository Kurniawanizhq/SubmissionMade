package com.eone.favorite.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eone.core.domain.model.Content
import com.eone.core.utils.Callback
import com.eone.core.ui.ContentAdapter
import com.eone.submissionmade.detail.DetailActivity
import com.eone.core.utils.SortUtils
import com.eone.favorite.FavoriteViewModel
import com.eone.favorite.ViewModelFactory
import com.eone.favorite.databinding.FragmentFavoriteMovieBinding
import com.eone.favorite.di.DaggerFavoriteComponent
import com.eone.favorite.utils.SwipeHelperCallback
import com.eone.submissionmade.di.FavoriteModulDependencies
import com.eone.favorite.utils.OnItemSwiped
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteMovieFragment : Fragment(), Callback {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var adapter: ContentAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: FavoriteViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerFavoriteComponent.builder()
            .context(context as Context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context as Context,
                    FavoriteModulDependencies::class.java
                )
            )
            .build()
            .injectMovies(this)
        _binding = FragmentFavoriteMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemSwipeHelper.attachToRecyclerView(binding.rvFavoriteMovie)
        adapter = ContentAdapter(this)
        getFavorite()
    }


    private val itemSwipeHelper = SwipeHelperCallback(object : OnItemSwiped {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val itemPositionMovie = adapter.getSwipedData(swipedPosition)
                var state = itemPositionMovie.favorite
                viewModel.setFavorite(itemPositionMovie, !state)
                state = !state
                val snackBar =
                    Snackbar.make(
                        view as View,
                        "Undelete previous favorite content?",
                        Snackbar.LENGTH_LONG
                    )
                snackBar.setAction("OK") {
                    viewModel.setFavorite(itemPositionMovie, !state)
                }
                snackBar.show()
            }
        }
    })

    private fun getFavorite() {
        binding.apply {
            rvFavoriteMovie.layoutManager = GridLayoutManager(context, 2)
            rvFavoriteMovie.adapter = adapter
            viewModel.getFavoriteMovies(SortUtils.POPULARITY).observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    showEmptyFavorite(false)
                    rvFavoriteMovie.adapter.let { adapter ->
                        if (adapter is ContentAdapter) {
                            adapter.setContent(it)
                        }
                    }
                } else {
                    showEmptyFavorite(true)
                }
            }
        }
    }

    private fun showEmptyFavorite(state: Boolean) {
        binding.apply {
            rvFavoriteMovie.isInvisible = state
            imgEmpty.isInvisible = !state
            titleEmptyState.isInvisible = !state
            descEmpty.isInvisible = !state
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(content: Content) {
        startActivity(
            Intent(context, DetailActivity::class.java)
                .putExtra(DetailActivity.EXTRA_DETAIL, content)
        )
    }

}