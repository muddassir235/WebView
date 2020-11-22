package com.muddassir.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*

interface UrlLoadListener {
    fun onUrlLoaded()
}

class MainActivity : AppCompatActivity(), UrlLoadListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webClient = WebClient()
        webClient.urlLoadListener = this
        webView.setWebViewClient(webClient)
        val set: WebSettings = webView.getSettings()
        set.javaScriptEnabled = true
        set.builtInZoomControls = true

        goOrRefreshIB.setOnClickListener {
            val url = formatUrl(urlInputEt.text.toString())

            if(webView.url == url) {
                webView.reload()
            } else {
                webView.loadUrl(url)
            }

            goOrRefreshIB.setImageResource(R.drawable.ic_refresh)

            refreshForwardBackButtons()
        }

        urlInputEt.addTextChangedListener {
            if(it.toString() != webView.url) {
                goOrRefreshIB.setImageResource(R.drawable.ic_go)
            } else {
                goOrRefreshIB.setImageResource(R.drawable.ic_refresh)
            }
        }

        backIB.setOnClickListener {
            webView.goBack()
            urlInputEt.setText(webView.url)

            refreshForwardBackButtons()
        }

        forwardIB.setOnClickListener {
            webView.goForward()
            urlInputEt.setText(webView.url)

            refreshForwardBackButtons()
        }
    }

    fun formatUrl(url: String): String {
        var formattedUrl = url

        if(!formattedUrl.startsWith("www.") && !formattedUrl.startsWith("http://")){
            formattedUrl = "www." + formattedUrl;
        }
        if(!formattedUrl.startsWith("http://")){
            formattedUrl = "http://"+formattedUrl;
        }

        return formattedUrl
    }

    fun enableDisableButton(enabled: Boolean, button: ImageButton) {
        button.isClickable = enabled
        button.alpha = if (enabled) 1.0f else 0.5f
    }

    fun refreshForwardBackButtons() {
        forwardIB.visibility = View.VISIBLE
        backIB.visibility = View.VISIBLE
        Log.e(MainActivity::javaClass.name, "webView.canGoForward(): " +
                webView.canGoForward()+", webView.canGoBack(): " + webView.canGoBack())
        enableDisableButton(webView.canGoForward(), forwardIB)
        enableDisableButton(webView.canGoBack(), backIB)
    }

    override fun onUrlLoaded() {
        this.refreshForwardBackButtons()
        urlInputEt.setText(webView.url)
    }

    internal class WebClient : WebViewClient() {
        var urlLoadListener: UrlLoadListener? = null

        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            urlLoadListener?.onUrlLoaded()
        }
    }
}