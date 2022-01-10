package com.anietie.moviezone.ui.movieslist.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.data.local.model.Resource
import com.anietie.moviezone.databinding.FragmentDiscoverMoviesBinding
import com.anietie.moviezone.di.Injection
import com.anietie.moviezone.di.ViewModelFactory
import com.anietie.moviezone.utils.ItemOffsetDecoration
import com.google.android.material.tabs.TabLayout

class DiscoverMoviesFragment : Fragment() {

    private lateinit var binding: FragmentDiscoverMoviesBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewModel: DiscoverMoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover_movies, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(this)
        setupListAdapter()
        setupTabLayout()
    }

    private fun setupListAdapter() {
        val recyclerView: RecyclerView = binding.discoverMoviesRecyclerView
        val discoverMoviesAdapter = DiscoverMoviesAdapter(viewModel)
        val layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.span_count))

        // draw network status and errors messages to fit the whole row(3 spans)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (discoverMoviesAdapter.getItemViewType(position)) {
                    R.layout.item_network_state -> layoutManager.spanCount
                    else -> 1
                }
            }
        }

        // setup recyclerView
        recyclerView.adapter = discoverMoviesAdapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        recyclerView.addItemDecoration(itemDecoration)

        // observe paged list
        viewModel.pagedList.observe(
            viewLifecycleOwner,
            Observer<PagedList<Movie>> { movies ->
                discoverMoviesAdapter.submitList(movies)
            }
        )

        // observe network state
        viewModel.networkState.observe(
            viewLifecycleOwner,
            Observer<Resource<*>> { resource -> discoverMoviesAdapter.setNetworkState(resource) }
        )
    }

    private fun setupTabLayout() {
        tabLayout = binding.movieCategoryTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Popular"))
        tabLayout.addTab(tabLayout.newTab().setText("Latest"))
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Popular" -> {
                        viewModel.setSortMoviesBy("Popular")
                    }
                    "Latest" -> {
                        viewModel.setSortMoviesBy("Latest")
                    }
                    "Upcoming" -> {
                        viewModel.setSortMoviesBy("Upcoming")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    companion object {

        fun obtainViewModel(fragment: Fragment): DiscoverMoviesViewModel {
            val factory: ViewModelFactory = Injection.provideViewModelFactory(fragment.requireContext())
            return ViewModelProviders.of(fragment, factory)[DiscoverMoviesViewModel::class.java]
        }
    }
}
