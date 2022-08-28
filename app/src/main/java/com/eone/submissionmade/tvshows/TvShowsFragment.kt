package com.eone.submissionmade.tvshows

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eone.core.data.source.Resource
import com.eone.core.domain.model.Content
import com.eone.core.ui.ContentAdapter
import com.eone.core.utils.Callback
import com.eone.core.utils.SortUtils
import com.eone.submissionmade.R
import com.eone.submissionmade.content.ContentViewModel
import com.eone.submissionmade.databinding.FragmentTvShowsBinding
import com.eone.submissionmade.detail.DetailActivity
import com.eone.submissionmade.home.MainActivity
import com.eone.submissionmade.utils.DataState
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TvShowsFragment : Fragment(), Callback {
    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ContentViewModel by viewModels()
    private lateinit var contentAdapter: ContentAdapter
    private lateinit var searchView: MaterialSearchView
    private var sort = SortUtils.RANDOM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowsBinding.inflate(layoutInflater, container, false)
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        searchView = (activity as MainActivity).findViewById(R.id.search_view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity != null) {
            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.search_menu, menu)
                    searchView.setMenuItem(menu.findItem(R.id.action_search))
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return false
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED)

            setupView()
            setList(sort)
            setSearchList()
            observeSearch()
        }
    }

    private fun observeSearch() {
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.queryChannel.value = it }
                return true
            }
        })
    }

    private fun setupView() {
        binding.apply {
            contentAdapter = ContentAdapter(this@TvShowsFragment)

            with(rvTvshow) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentAdapter
            }

            fabPopularity.setOnClickListener {
                fabMenu.close(true)
                sort = SortUtils.POPULARITY
                setList(sort)
            }
            fabNewest.setOnClickListener {
                fabMenu.close(true)
                sort = SortUtils.NEWEST
                setList(sort)
            }
            fabVote.setOnClickListener {
                fabMenu.close(true)
                sort = SortUtils.VOTE
                setList(sort)
            }
            fabRandom.setOnClickListener {
                fabMenu.close(true)
                sort = SortUtils.RANDOM
                setList(sort)
            }
        }
    }

    private fun setList(sort: String) {
        viewModel.getTvShows(sort).observe(viewLifecycleOwner,observer)
    }

    private val observer = Observer<Resource<List<Content>>> {
        if (it != null) {
            when (it) {
                is Resource.Loading -> setDataState(DataState.LOADING)
                is Resource.Success -> {
                    setDataState(DataState.SUCCESS)
                    contentAdapter.setContent(it.data)
                }
                is Resource.Error -> setDataState(DataState.ERROR)
            }
        }
    }

    private fun setSearchList() {
        viewModel.tvShowResult.observe(viewLifecycleOwner) { tvShows ->
            if (tvShows.isNullOrEmpty()) {
                setDataState(DataState.ERROR)
            } else {
                setDataState(DataState.SUCCESS)
            }
            contentAdapter.setContent(tvShows)
        }

        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                setDataState(DataState.SUCCESS)
            }

            override fun onSearchViewClosed() {
                setDataState(DataState.SUCCESS)
                setList(sort)
            }
        })
    }

    private fun setDataState(state: DataState) {
        binding.apply {
            when (state) {
                DataState.ERROR -> {
                    showLoading(false)
                    ivNoData.visibility = View.VISIBLE
                    tvNoData.visibility = View.VISIBLE
                }
                DataState.LOADING -> {
                    showLoading(true)
                    ivNoData.visibility = View.GONE
                    tvNoData.visibility = View.GONE
                }
                DataState.SUCCESS -> {
                    showLoading(false)
                    ivNoData.visibility = View.GONE
                    tvNoData.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
        searchView.setOnSearchViewListener(null)
        binding.rvTvshow.adapter = null
        _binding = null
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.rlTvShows.start()
        } else {
            binding.rlTvShows.stop()
        }
    }

    override fun onItemClicked(content: Content) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DETAIL, content)
        startActivity(intent)
    }
}