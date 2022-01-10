package com.anietie.moviezone.ui.moviedetails.reviews

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Review

object ReviewsBinding {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Review>?) {
        val adapter = recyclerView.adapter as ReviewsRecyclerAdapter
        if (items != null) {
            adapter.submitList(items)
        }
    }
}
