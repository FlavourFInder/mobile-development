package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ItemCardHomeBinding
import com.example.flavorfinder.network.response.FilterItem

class FilteredMealListAdapter : ListAdapter<FilterItem, FilteredMealListAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(val binding: ItemCardHomeBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: FilteredMealListAdapter.ViewHolder, position: Int) {
        val mealItem = getItem(position)
        mealItem?.let { meal ->
            Glide.with(holder.itemView.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.default_image)
                .into(holder.binding.ivItemCard)
            holder.binding.tvItemCard.text = meal.strMeal
            holder.itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(meal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredMealListAdapter.ViewHolder {
        val binding = ItemCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FilterItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilterItem>() {
            override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
