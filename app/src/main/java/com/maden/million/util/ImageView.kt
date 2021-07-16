package com.maden.million.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.maden.million.R
import com.squareup.picasso.Picasso

fun ImageView.downloadPhoto(url: String) {

    if(url != "" && url != null){
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_profile_place_holder)
            .error(R.drawable.ic_profile_place_holder)
            .into(this)
    }
}

@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url: String){
    if(url != "" && url != null){
        view.downloadPhoto(url)
    }
}