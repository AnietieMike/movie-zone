package com.anietie.moviezone.ui.movieslist.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Movie
import com.anietie.moviezone.databinding.FragmentFavouriteMoviesBinding
import com.anietie.moviezone.di.Injection
import com.anietie.moviezone.di.ViewModelFactory
import com.anietie.moviezone.ui.movieslist.discover.DiscoverMoviesFragment.Companion.obtainViewModel
import com.anietie.moviezone.utils.ItemOffsetDecoration

class FavouriteMoviesFragment : Fragment() {

    private var _binding: FragmentFavouriteMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(this)
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        toolbar.title = "Favourites"
    }

    private fun setupListAdapter() {
        val recyclerView: RecyclerView = binding.favouritesRecyclerView
        val favoritesAdapter = FavoritesAdapter()
        val layoutManager = GridLayoutManager(activity, 2)

        // setup recyclerView
        recyclerView.adapter = favoritesAdapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        recyclerView.addItemDecoration(itemDecoration)

        // observe favorites list
        viewModel.favoriteListLiveData.observe(
            viewLifecycleOwner,
            Observer<List<Movie>> { movieList ->
                if (movieList.isEmpty()) {
                    // display empty state since there is no favorites in database
                    binding.favouritesRecyclerView.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                } else {
                    binding.favouritesRecyclerView.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                    favoritesAdapter.submitList(movieList)
                }
            }
        )
    }

    private fun obtainViewModel(fragment: Fragment): FavoritesViewModel {
        val factory: ViewModelFactory = Injection.provideViewModelFactory(fragment.requireContext())
        return ViewModelProviders.of(fragment, factory)[FavoritesViewModel::class.java]
    }
}
