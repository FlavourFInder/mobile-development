package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ItemCardHomeBinding
import com.example.flavorfinder.network.response.MealsItem

class MealListAdapter: PagingDataAdapter<MealsItem, MealListAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(val binding: ItemCardHomeBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onBindViewHolder(holder: MealListAdapter.ViewHolder, position: Int) {
        val mealItem = getItem(position)
        mealItem?.let { meal ->
            Glide.with(holder.itemView.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.default_image)
                .into(holder.binding.ivItemCard)
            holder.binding.tvItemCard.text = meal.strMeal
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(meal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListAdapter.ViewHolder {
        val binding = ItemCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MealsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MealsItem>() {
            override fun areItemsTheSame(oldItem: MealsItem, newItem: MealsItem): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: MealsItem, newItem: MealsItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}