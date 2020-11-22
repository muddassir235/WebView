package com.muddassir.myapplication

import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.setWebViewClient(WebClient())
        val set: WebSettings = webView.getSettings()
        set.javaScriptEnabled = true
        set.builtInZoomControls = true

        goOrRefreshIB.setOnClickListener {
            val url = urlInputEt.text.toString()

            loadUrl(url)
        }

        backIB.setOnClickListener {
            webView.goBack()
        }

        forwardIB.setOnClickListener {
            webView.goForward()
        }
    }

    fun loadUrl(url: String) {
        if(URLUtil.isValidUrl(url)) {
            webView.loadUrl(url)
        } else {
            Toast.makeText(this, "Invalid Url!", Toast.LENGTH_SHORT).show()
        }
    }

    internal class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}