package com.rexmicrosystems.filesharingapp

import android.graphics.pdf.PdfRenderer
import android.media.MediaParser.SeekableInputReader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webView)
        val fileName = intent.getStringExtra("EXTRA_FILENAME")
        //val fileURLString = "https://firebasestorage.googleapis.com/v0/b/rexmicrosystems.appspot.com/o/FileSharingApp%2FResume%20-%20Jolly%20Bangue%20-%20M.Eng%20-%20Mobile%20App%20Developer.pdf?alt=media&token=bb99abbf-fff7-48a6-b3e3-4770d1d554aa&_gl=1*bztqki*_ga*MjAzOTI0ODQxNi4xNjkzMzY1NjQ0*_ga_CW55HF8NVT*MTY5OTMyODMxMC42NS4xLjE2OTkzMjgzMTguNTIuMC4w"
        val fileURLString = "https://www.pdf995.com/samples/pdf.pdf"
        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView.webViewClient = WebViewClient()
//        webView.webChromeClient = WebChromeClient()
        webView.settings.setSupportZoom(true)
        //webView.settings.builtInZoomControls = true

        //webView.settings.javaScriptEnabled = true

//        webView.webViewClient = object: WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(fileURLString)
//                return true
//            }
//        }
        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=https://firebasestorage.googleapis.com/v0/b/rexmicrosystems.appspot.com/o/FileSharingApp%2FResume%20-%20Jolly%20Bangue%20-%20M.Eng%20-%20Mobile%20App%20Developer.pdf?alt=media&token=bb99abbf-fff7-48a6-b3e3-4770d1d554aa&_gl=1*bztqki*_ga*MjAzOTI0ODQxNi4xNjkzMzY1NjQ0*_ga_CW55HF8NVT*MTY5OTMyODMxMC42NS4xLjE2OTkzMjgzMTguNTIuMC4w")
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$fileURLString")

//        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$fileURLString")
//        webView.loadUrl(fileURLString)

    }
}