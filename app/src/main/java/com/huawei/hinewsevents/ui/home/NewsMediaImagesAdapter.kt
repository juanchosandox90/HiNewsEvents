package com.huawei.hinewsevents.ui.home

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.DialogUtils
import com.huawei.hinewsevents.utils.extension.Utils

class NewsMediaImagesAdapter(private val newsMediaUrlList: List<String>) :
RecyclerView.Adapter<NewsMediaImagesAdapter.ViewHolder>() {

    val TAG: String = NewsMediaImagesAdapter::class.simpleName.toString()

    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_list_item_media, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsMediaUrlList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var imageUri: String =
            Utils.getDefaultImageUri(holder.item.context, R.drawable.notfound.toString())

        Log.d(TAG, "newsMediaUrlList[$position] :${newsMediaUrlList[position]}")

        if( newsMediaUrlList[position] != null && !newsMediaUrlList[position].contains(" ") ){
            imageUri = Uri.parse(newsMediaUrlList[position]).toString()
        }

        Utils.loadAndSetImageWithGlide(
            holder.item.context,
            holder.item.findViewById(R.id.media_item_image),
            imageUri
        )

        holder.item.setOnClickListener {
            DialogUtils.showDialogImagePeekPopView(
                holder.item.context,
                imageUri,
                imageUri
            )
        }
    }

}