package com.orioltobar.androidklean.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import javax.inject.Inject

abstract class BaseViewHolder<T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
    RecyclerView.ViewHolder(parent.inflate(layoutId)), LayoutContainer {

    override val containerView: View?
        get() = itemView

    abstract fun update(model: T)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)