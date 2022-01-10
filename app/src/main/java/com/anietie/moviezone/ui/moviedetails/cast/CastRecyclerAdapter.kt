package com.anietie.moviezone.ui.moviedetails.cast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anietie.moviezone.R
import com.anietie.moviezone.data.local.model.Cast
import com.anietie.moviezone.databinding.ItemCastBinding
import com.anietie.moviezone.utils.Constants
import com.bumptech.glide.Glide

class CastRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var castList: List<Cast>

    class CastViewHolder(private val binding: ItemCastBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(cast: Cast) {
            val profileImage: String =
                Constants.IMAGE_BASE_URL + Constants.PROFILE_SIZE_W185 + cast.profileImagePath
            Glide.with(context)
                .load(profileImage)
                .placeholder(R.color.blue_grey_200)
                .dontAnimate()
                .into(binding.imageCastProfile)
            binding.textCastName.text = cast.actorName
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): CastViewHolder {
                // Inflate
                val layoutInflater = LayoutInflater.from(parent.context)
                // Create the binding
                val binding: ItemCastBinding =
                    ItemCastBinding.inflate(layoutInflater, parent, false)
                return CastViewHolder(binding, parent.context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CastViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cast: Cast = castList[position]
        (holder as CastViewHolder).bindTo(cast)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    fun submitList(casts: List<Cast>) {
        castList = casts
        notifyDataSetChanged()
    }
}
