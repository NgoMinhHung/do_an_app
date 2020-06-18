package com.minhhung.life_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.holders.RemovableCategoryViewHolder
import java.util.*

class RemovableCategoriesAdapter(private val items: MutableList<String> = mutableListOf()) :
    RecyclerView.Adapter<RemovableCategoryViewHolder>() {
    fun addCategory(categoryName: String) {
        if (!items.map { it.toLowerCase(Locale.ROOT).trim() }.contains(categoryName.toLowerCase(Locale.ROOT).trim())) {
            items.add(categoryName)
            notifyDataSetChanged()
        }
    }

    private fun removeCategory(categoryName: String) {
        items.remove(categoryName)
        notifyDataSetChanged()
    }

    fun getCategories() = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemovableCategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_removable_category, parent, false)
        return RemovableCategoryViewHolder(view).apply {
            onDelete = {
                removeCategory(it)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RemovableCategoryViewHolder, position: Int) {
        holder.item = items[position]
    }
}