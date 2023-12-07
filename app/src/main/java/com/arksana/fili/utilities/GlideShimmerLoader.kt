package com.arksana.fili.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable


class GlideShimmerLoader {

    object GlideShimmerLoader {
        fun loadWithShimmer(context: Context?, imageUrl: String?, imageView: ImageView) {
            val shimmer = Shimmer.ColorHighlightBuilder()
                .setBaseColor(context?.resources?.getColor(android.R.color.white)!!)
                .setBaseAlpha(1f)
                .setHighlightColor(context.resources.getColor(android.R.color.darker_gray))
                .setHighlightAlpha(1f)
                .setDropoff(
                    50f
                )
                .build()
            val shimmerDrawable = ShimmerDrawable()
            shimmerDrawable.setShimmer(shimmer)
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(shimmerDrawable)
                .error(shimmerDrawable)
            Glide.with(context!!)
                .load(imageUrl)
                .apply(requestOptions)
                .into(object : DrawableImageViewTarget(imageView) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        imageView.setImageDrawable(errorDrawable)
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        imageView.setImageDrawable(resource)
                    }
                })
        }
    }
}