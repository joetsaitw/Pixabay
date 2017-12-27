package org.joetsai.pixabay.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.joetsai.pixabay.R
import org.joetsai.pixabay.util.inflate


class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_progressbar))
}