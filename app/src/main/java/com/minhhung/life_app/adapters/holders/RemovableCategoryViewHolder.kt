package com.minhhung.life_app.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_removable_category.*

class RemovableCategoryViewHolder(override val containerView: View?) :
    RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    var item: String? = null
        set(value) {
            field = value
            display()
        }

    var onDelete: ((String) -> Unit)? = null

    init {
        btnDelete.setOnClickListener {
            item?.let {
                onDelete?.invoke(it)
            }
        }
    }

    private fun display() {
        item?.let {
            tvCategoryName.text = it
        }
    }
}