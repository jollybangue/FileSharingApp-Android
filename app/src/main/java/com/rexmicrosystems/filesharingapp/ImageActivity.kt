package com.rexmicrosystems.filesharingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        var fileName = "Name of the Image"
        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Adding a back to Home button in the left side of the AppBar. To make the back button functional, I added "android:parentActivityName=".HomeActivity"" in the AndroidManifest.xml file, at the section of ImageActivity.
    }
}