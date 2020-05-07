package com.napptilians.doy.view.categorylist

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.category_item.view.categoryImage
import kotlinx.android.synthetic.main.category_item.view.categoryOpacity
import kotlinx.android.synthetic.main.category_item.view.categoryText
import kotlinx.android.synthetic.main.category_item.view.tickImageView

class CategoryItemViewHolder constructor(parent: ViewGroup, private val isAddingService: Boolean) :
    BaseViewHolder<CategoryModel>(parent, R.layout.category_item) {

    override fun update(model: CategoryModel) {
        itemView.categoryText.text = model.name
        itemView.categoryImage.clipToOutline = true
        if (isAddingService) {
            if (model.isSelected) {
                itemView.tickImageView.visible()
                itemView.categoryImage.alpha = 1f
            } else {
                itemView.tickImageView.gone()
                itemView.categoryImage.alpha = 0.5f
            }
        } else {
            itemView.tickImageView.gone()
            itemView.categoryImage.alpha = 1f
        }
        Glide.with(itemView)
            .load(model.pictureUrl)
            .placeholder(R.color.colorAccent)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirst: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirst: Boolean
                ): Boolean {
                    itemView.categoryOpacity.visible()
                    return false
                }
            })
            .into(itemView.categoryImage)
    }
}
