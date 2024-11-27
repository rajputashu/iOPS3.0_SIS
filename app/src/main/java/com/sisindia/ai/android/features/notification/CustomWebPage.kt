package com.sisindia.ai.android.features.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sisindia.ai.android.R
import com.sisindia.ai.android.databinding.ActivityCustomWebpageBinding

class CustomWebPage : AppCompatActivity() {

    lateinit var binding : ActivityCustomWebpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_webpage)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_webpage)

        intent.extras?.apply {
            binding.selfServiceMenu.text = this.getString("NOTIFICATION_TITLE", "")
            val url = this.getString("NOTIFICATION_URL", "")
            if (!url.isNullOrEmpty()) {
                initWeb(url)
            } else {
                binding.notificationWVProgress.visibility = View.GONE
                binding.notificationWVErrorMsg.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb(webURL: String) {
        binding.notificationWVProgress.visibility = View.VISIBLE
        binding.customNotificationWebView.settings.javaScriptEnabled = true
        binding.customNotificationWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.notificationWVProgress.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?,
                error: WebResourceError?) {
                binding.notificationWVProgress.visibility = View.GONE
                binding.notificationWVErrorMsg.visibility = View.VISIBLE
            }
        }
        binding.customNotificationWebView.loadUrl(webURL)
    }
}