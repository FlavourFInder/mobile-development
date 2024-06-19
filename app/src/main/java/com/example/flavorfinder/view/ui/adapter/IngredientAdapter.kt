package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorfinder.databinding.ItemIngredientBinding

class IngredientAdapter(private val ingredientList: List<String>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = ingredientList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvIngredientName.text = ingredientList[position]
    }

}