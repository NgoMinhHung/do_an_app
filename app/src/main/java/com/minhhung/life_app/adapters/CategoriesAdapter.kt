package com.minhhung.life_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.holders.CategoryViewHolder
import com.minhhung.life_app.models.Category

class CategoriesAdapter(private val items: MutableList<Category> = mutableListOf()) :
    RecyclerView.Adapter<CategoryViewHolder>() {
    fun setCategories(categories: MutableList<Category> = mutableListOf()) {
        items.clear()
        items.addAll(categories)
        notifyDataSetChanged()
    }

    fun addCategory(categoryName: String) {
        items.add(Category(1, categoryName, ""))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.item = items[position]
    }
}