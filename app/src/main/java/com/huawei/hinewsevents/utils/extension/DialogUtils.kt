package com.huawei.hinewsevents.utils.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.huawei.hinewsevents.R

class DialogUtils {

    companion object {

        private var dialog: Dialog? = null
        fun showDialogImagePeekPopView(context: Context, imageUri: String, title: String) {
            val dialogLayout =
                LayoutInflater.from(context).inflate(R.layout.dialog_image_peek_pop, null)
            val imageView = dialogLayout.findViewById<ImageView>(R.id.image)
            val textView = dialogLayout.findViewById<TextView>(R.id.text_img_name)
            dialog = Dialog(context)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(dialogLayout)
            dialog?.setCanceledOnTouchOutside(true)
            dialog?.setCancelable(true)
            textView.text = title
            Utils.loadAndSetImageWithGlide(context, imageView, imageUri)
            dialog?.show()
        }

        fun hideQuickView() {
            dialog?.dismiss()
        }

    }

}