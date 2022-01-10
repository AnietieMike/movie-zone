package com.anietie.moviezone.ui.moviedetails.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.anietie.moviezone.data.local.model.Review
import com.anietie.moviezone.databinding.ItemReviewBinding
import java.util.Locale

class ReviewsRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var reviewList: List<Review>

    class ReviewsViewHolder(binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemReviewBinding
        fun bindTo(review: Review) {
            val userName = review.author

            // review user image
            val generator = ColorGenerator.MATERIAL
            val color = generator.randomColor
            val drawable = TextDrawable.builder()
                .buildRound(userName?.substring(0, 1)?.uppercase(Locale.ROOT), color)
            binding.imageAuthor.setImageDrawable(drawable)

            // review's author
            binding.textAuthor.text = userName

            // review's content
            binding.textContent.text = review.content
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): ReviewsViewHolder {
                // Inflate
                val layoutInflater = LayoutInflater.from(parent.context)
                // Create the binding
                val binding: ItemReviewBinding =
                    ItemReviewBinding.inflate(layoutInflater, parent, false)
                return ReviewsViewHolder(binding)
            }
        }

        init {
            this.binding = binding
            binding.frame.clipToOutline = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review: Review = reviewList[position]
        (holder as ReviewsViewHolder).bindTo(review)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    fun submitList(reviews: List<Review>) {
        reviewList = reviews
        notifyDataSetChanged()
    }
}
