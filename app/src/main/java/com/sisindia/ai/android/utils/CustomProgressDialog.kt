package com.sisindia.ai.android.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import com.sisindia.ai.android.R
//import kotlinx.android.synthetic.main.progress_dialog.view.*

/**
 * Created by Ashu Rajput on 4/3/2021.
 */
class CustomProgressDialog {
    companion object {
        @SuppressLint("InflateParams")
        fun buildDialog(context: Context, message: String): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            val messageTV: AppCompatTextView = inflate.findViewById(R.id.loadingMessage)
//            inflate.loadingMessage.text = message
            messageTV.text = message
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}