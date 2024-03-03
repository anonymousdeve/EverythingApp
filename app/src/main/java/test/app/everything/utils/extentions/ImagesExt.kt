package test.app.everything.utils.extentions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import test.app.everything.R

@BindingAdapter("bind:url")
fun ImageView.loadCircleImage(url: String?) {
    this.load(url) {
        // Placeholder image while loading
        placeholder(R.drawable.pleace_holder)
        transformations(CircleCropTransformation())
        error(R.drawable.pleace_holder)  // error place holder image
        crossfade(true) // Enable crossfade animation
    }
}

@BindingAdapter("bind:loadImage")
fun ImageView.loadRectangleImage(url: String?) {
    this.load(url) {
        placeholder(R.drawable.pleace_holder) // Placeholder image while loading
        error(R.drawable.pleace_holder)  // error place holder image
        crossfade(true) // Enable crossfade animation
    }
}