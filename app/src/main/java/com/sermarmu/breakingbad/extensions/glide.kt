package com.sermarmu.breakingbad.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadImageFromUrl(
    url: String,
    transition: DrawableTransitionOptions = DrawableTransitionOptions.withCrossFade()
) {
    Glide.with(context)
        .load(url)
        .transition(transition)
        .into(this)
}