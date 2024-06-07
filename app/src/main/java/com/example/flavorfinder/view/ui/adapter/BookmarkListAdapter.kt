package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ItemCardBookmarkBinding
import com.example.flavorfinder.network.response.GetBookmarkRecipe

class BookmarkListAdapter : ListAdapter<GetBookmarkRecipe, BookmarkListAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemCardBookmarkBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mealItem = getItem(position)
        mealItem?.let { meal ->
            Glide.with(holder.itemView.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.default_image)
                .into(holder.binding.ivItemCard)
            holder.binding.tvMenuName.text = meal.strMeal
            holder.binding.tvDescription.text = meal.strInstructions
            holder.itemView.setOnClickListener {
                onItemClickCallback?.onItemClick(meal)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: GetBookmarkRecipe)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetBookmarkRecipe>() {
            override fun areItemsTheSame(oldItem: GetBookmarkRecipe, newItem: GetBookmarkRecipe): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: GetBookmarkRecipe, newItem: GetBookmarkRecipe): Boolean {
                return oldItem == newItem
            }
        }
    }
}
