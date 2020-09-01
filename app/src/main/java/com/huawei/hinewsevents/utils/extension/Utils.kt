package com.huawei.hinewsevents.utils.extension

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.huawei.hinewsevents.R
import com.huawei.hms.ads.BannerAdSize
import java.util.*

open class Utils {

    companion object {
        private val TAG = Utils::class.java.simpleName

        fun showToastMessage(
            appContext: Context?,
            message: String?
            ) {
                Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
            }

        fun showDialogOK(
            appContext: Context?,
            message: String?,
            okListener: DialogInterface.OnClickListener?
        ) {
            AlertDialog.Builder(appContext!!)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
        }

        fun haveNetworkConnection(appContext: Context): Boolean {
            var haveConnected = false
            val cm =
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfos = cm.allNetworkInfo
            for (ni in networkInfos) {
                if (ni != null && ni.isConnected) {
                    haveConnected = true
                }
            }
            return haveConnected
        }

        fun getDefaultImageUri(appContext: Context, drawableSource: String): String {
            return Uri.parse(
                "android.resource://" + appContext.packageName + "/" + drawableSource
            ).toString()
        }

        fun loadAndSetImageWithGlide(
            appContext: Context?,
            imageView: ImageView?,
            uriOrUrl: String?
        ) {

            // TODO get fix load image with drawable resource
            Glide.with(appContext!!) //.asBitmap()
                .load(uriOrUrl)
                .centerCrop()
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .thumbnail(Glide.with(appContext).load(R.drawable.gif_loading).centerInside())
                .error(Glide.with(appContext).load(R.drawable.notfound).centerInside())
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i(TAG, "Glide onLoadFailed : " + e!!.message)
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i(TAG, "Glide onResourceReady : $dataSource")
                        return false
                    }
                })
                .into(imageView!!)
        }


        fun getRandomNumber(max: Int, min: Int): Int {
            return Random()
                .nextInt(max - min + 1) + min
        }

        fun getRandomDouble(max: Int, min: Int): Double {
            return min + Random().nextDouble() * (max - min);
        }

        fun getColorRatingLevel(raiting: Int): Int {
            if (raiting < 100) {
                return R.color.red700
            } else if (raiting < 500) {
                return R.color.pink600
            } else if (raiting < 1000) {
                return R.color.deeporange600
            } else if (raiting < 5000){
                return R.color.amber600
            } else if (raiting < 10000) {
                return R.color.teal600
            } else {
                return R.color.green600
            }
        }

        fun getBannerAdSize(whichSize: Int): BannerAdSize? {
            var adSize: BannerAdSize? = null
            adSize = when (whichSize) {
                1 -> BannerAdSize.BANNER_SIZE_320_50
                2 -> BannerAdSize.BANNER_SIZE_320_100
                3 -> BannerAdSize.BANNER_SIZE_300_250
                4 -> BannerAdSize.BANNER_SIZE_360_144
                5 -> BannerAdSize.BANNER_SIZE_360_57
                6 -> BannerAdSize.BANNER_SIZE_SMART
                7 -> BannerAdSize.BANNER_SIZE_DYNAMIC
                else -> BannerAdSize.BANNER_SIZE_320_100
            }
            Log.d(TAG, "getBannerAdSize()....whichSize : $whichSize - $adSize")
            return adSize
        }

        fun getBannerViewBackground(whichColor: Int): Int {
            var color = Color.TRANSPARENT
            color = when (whichColor) {
                1 -> Color.BLACK
                2 -> Color.RED
                3 -> Color.BLUE
                4 -> Color.GREEN
                5 -> Color.YELLOW
                else -> Color.TRANSPARENT
            }
            return color
        }

    }

    // TODO remove after test
    private fun randomColorGenerator(): Int {
        return Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
    }
}