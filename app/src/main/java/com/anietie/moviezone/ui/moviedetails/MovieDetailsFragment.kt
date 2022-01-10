package com.anietie.moviezone.ui.moviedetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.MovieDetails
import com.anietie.moviezone.databinding.FragmentMovieDetailsBinding
import com.anietie.moviezone.di.Injection
import com.anietie.moviezone.di.ViewModelFactory
import com.anietie.moviezone.ui.moviedetails.cast.CastRecyclerAdapter
import com.anietie.moviezone.ui.moviedetails.reviews.ReviewsRecyclerAdapter
import com.anietie.moviezone.ui.moviedetails.trailers.TrailersRecyclerAdapter
import com.anietie.moviezone.utils.Constants
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId
        viewModel = obtainViewModel(this)
        Timber.tag("Viewmodel initialized").d("Initializing viewmodel... $movieId")
        viewModel.init(movieId)
        setupToolbar()
        setupTrailersAdapter()
        setupCastAdapter()
        setupReviewsAdapter()

        // observe result
        viewModel.result?.observe(
            viewLifecycleOwner,
            { resource ->
                if (resource.data?.movie != null
                ) {
                    viewModel.isFavorite = resource.data.movie!!.isFavorite
                }
                binding.resource = resource
                binding.setMovieDetails(resource.data)
            }
        )

        // handle retry event in case of network failure
        binding.networkStateView.retryButton.setOnClickListener {
            viewModel.retry(movieId)
        }

        // Observe snackbar messages
        viewModel.snackbarMessage.observe(
            viewLifecycleOwner,
            { message ->
                Snackbar.make(
                    binding.root,
                    message!!,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun setupToolbar() {
        var isFavorite = viewModel.isFavorite
        val toolbar: Toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.movie_details_menu)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        with(toolbar) {
            if (isFavorite) {
                menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_round_favorite_24)
                    .setTitle(R.string.action_remove_from_favorites)
                isFavorite = false
            }
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_round_favorite_border)
                .setTitle(R.string.action_remove_from_favorites)
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    val movieDetails: MovieDetails = viewModel.result?.value?.data!!
                    val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
                        .setType("text/plain")
                        .setSubject(movieDetails.movie?.title.toString() + " movie trailer")
                        .setText(
                            "Check out " + movieDetails.movie?.title
                                .toString() + " movie trailer at " +
                                Uri.parse(
                                    Constants.YOUTUBE_WEB_URL +
                                        movieDetails.trailers[0].key
                                )
                        )
                        .createChooserIntent()
                    var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                    flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    shareIntent.addFlags(flags)
                    if (shareIntent.resolveActivity(context?.packageManager!!) != null) {
                        startActivity(shareIntent)
                    }
                    true
                }
                R.id.action_favorite -> {
                    // Add or remove movie to/from favorites
                    viewModel.onFavoriteClicked()
                    true
                }
                else -> false
            }
        }
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        handleCollapsedToolbarTitle()
    }

    private fun setupTrailersAdapter() {
        val listTrailers: RecyclerView = binding.movieDetailsInfo.listTrailers
        listTrailers.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        listTrailers.setHasFixedSize(true)
        listTrailers.adapter = TrailersRecyclerAdapter()
        ViewCompat.setNestedScrollingEnabled(listTrailers, false)
    }

    private fun setupCastAdapter() {
        val listCast: RecyclerView = binding.movieDetailsInfo.listCast
        listCast.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        listCast.adapter = CastRecyclerAdapter()
        ViewCompat.setNestedScrollingEnabled(listCast, false)
    }

    private fun setupReviewsAdapter() {
        val listReviews: RecyclerView = binding.movieDetailsInfo.listReviews
        listReviews.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        listReviews.adapter = ReviewsRecyclerAdapter()
        ViewCompat.setNestedScrollingEnabled(listReviews, false)
    }

    /**
     * sets the title on the toolbar only if the toolbar is collapsed
     */
    private fun handleCollapsedToolbarTitle() {
        binding.appbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                // verify if the toolbar is completely collapsed and set the movie name as the title
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.title =
                        viewModel.result?.value?.data?.movie?.title
                    isShow = true
                } else if (isShow) {
                    // display an empty string when toolbar is expanded
                    binding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun obtainViewModel(fragment: Fragment): MovieDetailsViewModel {
        val factory: ViewModelFactory = Injection.provideViewModelFactory(fragment.requireContext())
        return ViewModelProviders.of(fragment, factory)[MovieDetailsViewModel::class.java]
    }
}
