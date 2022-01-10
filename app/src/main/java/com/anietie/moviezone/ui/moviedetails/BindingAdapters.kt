package com.anietie.moviezone.ui.moviedetails

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Cast
import com.anietie.moviezone.data.local.model.Genre
import com.anietie.moviezone.ui.moviedetails.cast.CastRecyclerAdapter
import com.anietie.moviezone.utils.Constants
import com.anietie.moviezone.utils.Extensions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object BindingAdapters {
    /**
     * Movie backdrop image
     *
     * */
    @JvmStatic
    @BindingAdapter("imageUrl", "isBackdrop")
    fun bindImage(imageView: ImageView, imagePath: String?, isBackdrop: Boolean) {
        val baseUrl = if (isBackdrop) {
            Constants.BACKDROP_URL
        } else {
            Constants.IMAGE_URL
        }
        Glide.with(imageView.context)
            .load(baseUrl + imagePath)
            .placeholder(R.color.blue_grey_200)
            .into(imageView)
    }

    /**
     * Movie details poster image
     */
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, imagePath: String?) {
        Glide.with(imageView.context)
            .load(Constants.IMAGE_URL + imagePath)
            .placeholder(R.color.blue_grey_200)
            .apply(
                RequestOptions().transforms(
                    CenterCrop(),
                    RoundedCorners(Extensions().dipToPixels(imageView.context, 8).toInt())
                )
            )
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        if (show) view.visibility = View.VISIBLE else view.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(view: ChipGroup, genres: List<Genre>?) {
        if (genres == null || // Since we are using liveData to observe data, any changes in that table(favorites)
            // will trigger the observer and hence rebinding data, which can lead to duplicates.
            view.childCount > 0
        ) return

        // dynamically create & add genre chips
        val context = view.context
        for (genre in genres) {
            val chip = Chip(context)
            chip.text = genre.name
            chip.setTextColor(context.resources.getColor(R.color.white))
            chip.chipStrokeWidth = Extensions().dipToPixels(context, 1)
            chip.chipStrokeColor = ColorStateList.valueOf(
                context.resources.getColor(R.color.blue_grey_200)
            )
            chip.setChipBackgroundColorResource(R.color.pink_400)
            view.addView(chip)
        }
    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Cast>?) {
        val adapter = recyclerView.adapter as CastRecyclerAdapter
        if (items != null) {
            adapter.submitList(items)
        }
    }
}
