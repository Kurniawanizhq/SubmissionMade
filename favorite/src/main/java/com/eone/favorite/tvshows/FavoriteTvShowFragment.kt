package com.eone.favorite.tvshows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eone.core.domain.model.Content
import com.eone.core.ui.ContentAdapter
import com.eone.core.utils.Callback
import com.eone.core.utils.SortUtils
import com.eone.favorite.FavoriteViewModel
import com.eone.favorite.ViewModelFactory
import com.eone.favorite.databinding.FragmentFavoriteTvShowBinding
import com.eone.favorite.di.DaggerFavoriteComponent
import com.eone.favorite.utils.OnItemSwiped
import com.eone.favorite.utils.SwipeHelperCallback
import com.eone.submissionmade.detail.DetailActivity
import com.eone.submissionmade.di.FavoriteModuleDependencies
import com.eone.submissionmade.utils.DataState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteTvShowFragment : Fragment(), Callback {

    private var _binding: FragmentFavoriteTvShowBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var sort = SortUtils.RANDOM
    private lateinit var contentAdapter : ContentAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel : FavoriteViewModel by viewModels{
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
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .injectTvShow(this)
        _binding = FragmentFavoriteTvShowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity != null) {
            itemSwipeHelper.attachToRecyclerView(binding.rvFavoriteTvshow)
            setupRecyclerView()
            setList(sort)
        }
    }


    private fun setList(sort : String){
        viewModel.getFavoriteTvShows(sort).observe(viewLifecycleOwner, observer)
    }

    private val observer = Observer<List<Content>> { content ->
        if (content.isNullOrEmpty()) {
            setDataState(DataState.ERROR)
        } else {
            setDataState(DataState.SUCCESS)
        }
        contentAdapter.setContent(content)
    }

    private fun setDataState(state: DataState) {
        binding.apply {
            when (state) {
                DataState.ERROR -> {
                    showEmptyFavorite(true)
                }
                DataState.LOADING -> {
                    showEmptyFavorite(true)
                }
                DataState.SUCCESS -> {
                    showEmptyFavorite(false)
                }
            }
        }
    }

    private fun setupRecyclerView(){
        contentAdapter = ContentAdapter(this)

        with(binding.rvFavoriteTvshow){
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = contentAdapter
        }
    }

    private val itemSwipeHelper = SwipeHelperCallback(object : OnItemSwiped {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val itemPositionMovie = contentAdapter.getSwipedData(swipedPosition)
                var state = itemPositionMovie.favorite
                viewModel.setFavorite(itemPositionMovie, !state)
                state = !state
                val snackBar = Snackbar.make(view as View,"Undelete previous favorite content?",Snackbar.LENGTH_LONG)
                snackBar.setAction("OK") {
                    viewModel.setFavorite(itemPositionMovie, !state)
                }
                snackBar.show()
            }
        }
    })

    private fun showEmptyFavorite(state: Boolean) {
        binding.apply {
            rvFavoriteTvshow.isInvisible = state
            imgEmpty.isInvisible = !state
            titleEmptyState.isInvisible = !state
            descEmpty.isInvisible = !state
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.rvFavoriteTvshow.adapter = null
        _binding = null
    }

    override fun onItemClicked(content: Content) {
        startActivity(
            Intent(context, DetailActivity::class.java)
                .putExtra(DetailActivity.EXTRA_DETAIL, content)
        )
    }

}