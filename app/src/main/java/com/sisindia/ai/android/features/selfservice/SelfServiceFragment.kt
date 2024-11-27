package com.sisindia.ai.android.features.selfservice

import android.annotation.SuppressLint
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentSelfServiceBinding

/**
 * Created by Ashu Rajput on 1/7/2021.
 */
class SelfServiceFragment : IopsBaseFragment() {

    private lateinit var viewModel: SelfServiceViewModel
    private lateinit var binding: FragmentSelfServiceBinding

    companion object {
        fun newInstance(): SelfServiceFragment = SelfServiceFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_self_service
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(SelfServiceViewModel::class.java) as SelfServiceViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this, Observer { message: Message ->
            when (message.what) {
                NavigationConstants.ON_FETCHING_TICKET_TOKEN -> initWeb()
            }
        })
    }

    override fun onCreated() {
        viewModel.getTicketToken()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb() {
        viewModel.isLoading.set(View.VISIBLE)
        binding.selfServiceWebView.settings.javaScriptEnabled = true
        binding.selfServiceWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModel.isLoading.set(View.GONE)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                viewModel.isWebPageLoaded.set(false)
            }
        }
        binding.selfServiceWebView.loadUrl(viewModel.webViewUrl.get().toString())
    }
}