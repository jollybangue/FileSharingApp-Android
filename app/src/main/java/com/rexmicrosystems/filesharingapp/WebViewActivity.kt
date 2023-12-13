package com.rexmicrosystems.filesharingapp

import android.graphics.pdf.PdfRenderer
import android.media.MediaParser.SeekableInputReader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.File

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webView)
        val fileName = intent.getStringExtra("EXTRA_FILENAME") // TODO: Get the file extension
        //val fileURLString = "https://www.pdf995.com/samples/pdf.pdf"
        //val tempFileForWebView = File.createTempFile(fileName.removeSuffix())

        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true

//        webView.settings.javaScriptEnabled = true
//        webView.settings.loadWithOverviewMode = true
//        webView.settings.useWideViewPort = true

        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=https://firebasestorage.googleapis.com/v0/b/rexmicrosystems.appspot.com/o/FileSharingApp%2FResume%20-%20Jolly%20Bangue%20-%20M.Eng%20-%20Mobile%20App%20Developer.pdf?alt=media&token=bb99abbf-fff7-48a6-b3e3-4770d1d554aa&_gl=1*bztqki*_ga*MjAzOTI0ODQxNi4xNjkzMzY1NjQ0*_ga_CW55HF8NVT*MTY5OTMyODMxMC42NS4xLjE2OTkzMjgzMTguNTIuMC4w")
        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$fileURLString")
        //webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$fileURLString")
        //webView.loadUrl(fileURLString)

        // The webView is unable to load any Firebase URL. So to load a Firebase Cloud Storage file in webView, we will:
        // 1- Download the file on the local device (in a temp folder...)
        // 2- Get the local temp URL of the file
        // 3- Load the local temp URL in the webView.



    }
}