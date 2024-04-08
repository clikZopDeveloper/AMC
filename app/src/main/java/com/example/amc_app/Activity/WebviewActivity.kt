package com.example.amc_app.Activity

import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.amc_app.R
import com.example.amc_app.databinding.ActivityWebviewBinding


class WebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
    //    setContentView(R.layout.activity_webview)

        binding.webView.webViewClient = WebViewClient()


        // this will load the url of the website
        binding.webView.loadUrl("http://smartpe.pro/User/Dashboard.aspx?Usrno=1")
        val url=intent.getStringExtra("url")
        binding.webView.loadUrl(url!!)
     //   binding.webView.loadUrl("https://developer.android.com/develop/ui/views/layout/webapps/webview")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.getSettings().setAllowContentAccess(true)
        binding.webView.getSettings().setAllowFileAccess(true)
        binding.webView.getSettings().setDatabaseEnabled(true)
        binding.webView.getSettings().setDomStorageEnabled(true)
        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                Log.d("aasdad", error?.toString()!!)
                handler.proceed() // Ignore SSL certificate errors
            }
        })
    }

    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}