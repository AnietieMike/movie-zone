package com.anietie.moviezone.ui.moviedetails.trailers

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Trailer

object TrailersBindingAdapter {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Trailer>?) {
        val adapter = recyclerView.adapter as TrailersRecyclerAdapter
        if (items != null) {
            adapter.submitList(items)
        }
    }
}
