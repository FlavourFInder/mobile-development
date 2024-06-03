package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ItemCardHomeBinding
import com.example.flavorfinder.network.response.MealsItem

class SearchResultAdapter(private val meals: List<MealsItem>) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCardHomeBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.default_image)
            .into(holder.binding.ivItemCard)
        holder.binding.tvItemCard.text = meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(meal)
        }
    }

    override fun getItemCount(): Int = meals.size

    interface OnItemClickCallback {
        fun onItemClicked(data: MealsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}
