package com.rexmicrosystems.filesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webView)
        val fileName = intent.getStringExtra("EXTRA_FILENAME")
        val fileURLString = "https://firebasestorage.googleapis.com/v0/b/rexmicrosystems.appspot.com/o/FileSharingApp%2FFile%203.jpg?alt=media&token=d27439f5-916f-4698-ae72-21122369939e&_gl=1*u0bh98*_ga*MjUzMDU0Mzg2LjE2ODkzNjkwNDg.*_ga_CW55HF8NVT*MTY5OTA0MDU2Ny4xNDguMS4xNjk5MDQyMzI5LjYwLjAuMA.."

        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        webView.webViewClient = WebViewClient()
//        webView.webChromeClient = WebChromeClient()

//        webView.webViewClient = object: WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(fileURLString)
//                return true
//            }
//        }

        webView.loadUrl(fileURLString)
    }
}