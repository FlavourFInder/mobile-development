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

class BookmarkListAdapter: ListAdapter<GetBookmarkRecipe, BookmarkListAdapter.ViewHolder>(DIFF_CallBACK) {
    class ViewHolder(val binding: ItemCardBookmarkBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkListAdapter.ViewHolder {
        val binding = ItemCardBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkListAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        dataItem?.let { data ->
            Glide.with(holder.itemView.context)
                .load(data.strMealThumb)
                .placeholder(R.drawable.default_image)
                .into(holder.binding.ivItemCard)
            holder.binding.tvMenuName.text = data.strMeal
            holder.binding.tvDescription.text = data.strInstructions
            holder.itemView.setOnClickListener {
                if (::onItemClickCallback.isInitialized) {
                    onItemClickCallback.onItemClick(data)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: GetBookmarkRecipe)
    }

    companion object {
        val DIFF_CallBACK = object : DiffUtil.ItemCallback<GetBookmarkRecipe>() {
            override fun areItemsTheSame(
                oldItem: GetBookmarkRecipe,
                newItem: GetBookmarkRecipe
            ): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(
                oldItem: GetBookmarkRecipe,
                newItem: GetBookmarkRecipe
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}