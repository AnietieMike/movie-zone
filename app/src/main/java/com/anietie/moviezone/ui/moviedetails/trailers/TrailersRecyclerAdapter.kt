package com.anietie.moviezone.ui.moviedetails.trailers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Trailer
import com.anietie.moviezone.databinding.ItemTrailerBinding
import com.anietie.moviezone.utils.Constants
import com.bumptech.glide.Glide

class TrailersRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var trailerList: List<Trailer>

    class TrailerViewHolder(binding: ItemTrailerBinding, context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemTrailerBinding
        private val context: Context
        fun bindTo(trailer: Trailer) {
            val thumbnail =
                "https://img.youtube.com/vi/" + trailer.key.toString() + "/hqdefault.jpg"
            Glide.with(context)
                .load(thumbnail)
                .placeholder(R.color.blue_grey_200)
                .into(binding.imageTrailer)
            binding.trailerName.text = trailer.title
            binding.cardTrailer.setOnClickListener {
                val appIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("vnd.youtube:" + trailer.key)
                )
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.YOUTUBE_WEB_URL + trailer.key)
                )
                if (appIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(appIntent)
                } else {
                    context.startActivity(webIntent)
                }
            }
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): TrailerViewHolder {
                // Inflate
                val layoutInflater = LayoutInflater.from(parent.context)
                // Create the binding
                val binding: ItemTrailerBinding =
                    ItemTrailerBinding.inflate(layoutInflater, parent, false)
                return TrailerViewHolder(binding, parent.context)
            }
        }

        init {
            this.binding = binding
            this.context = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrailerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val trailer = trailerList[position]
        (holder as TrailerViewHolder).bindTo(trailer)
    }

    override fun getItemCount() = trailerList.size

    fun submitList(trailers: List<Trailer>) {
        trailerList = trailers
        notifyDataSetChanged()
    }
}
