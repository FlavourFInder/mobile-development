package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorfinder.databinding.ItemIngredientBinding
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.view.ui.adapter.MealListAdapter.Companion.DIFF_CALLBACK

class IngredientAdapter(private val ingredientList: List<String>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredientList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvIngredientName.text = ingredientList[position]
    }

}