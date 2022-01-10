package com.anietie.moviezone.ui.moviedetails.cast

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.data.local.model.Cast

object CastBindingAdapter {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Cast>?) {
        val adapter: CastRecyclerAdapter = recyclerView.adapter as CastRecyclerAdapter
        if (items != null) {
            adapter.submitList(items)
        }
    }
}
