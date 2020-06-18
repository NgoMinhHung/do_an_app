package com.minhhung.life_app.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.minhhung.life_app.models.Category
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*

class CategoryViewHolder(override val containerView: View?) :
    RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    var item: Category? = null
        set(value) {
            field = value
            display()
        }

    private fun display() {
        item?.let {
            btnCategory.text = it.name
        }
    }
}