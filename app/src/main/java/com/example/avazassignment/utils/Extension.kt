package com.example.avazassignment.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.avazassignment.R
import com.google.gson.Gson

val gson = Gson()


/*
* for set food image in imageview
* */
@BindingAdapter("app:image_src")
fun ImageView.loadImage(path: String?) {
    try {
        if (!path.isNullOrEmpty()) {
            if (path.toIntOrNull() != null) {
                Glide.with(context).load(path.toInt()).into(this)
            } else {
                Glide.with(context).load(path).into(this)
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


/*
* for set check button visibility
* */
@BindingAdapter("app:visibility")
fun ImageView.setVisibility(isVisible: Boolean) {
    if (isVisible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}