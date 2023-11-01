package com.rexmicrosystems.filesharingapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val imageView = findViewById<ImageView>(R.id.imageView)

        var fileName = intent.getStringExtra("EXTRA_FILENAME")
        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Adding a back to Home button in the left side of the AppBar. To make the back button functional, I added "android:parentActivityName=".HomeActivity"" in the AndroidManifest.xml file, at the section of ImageActivity.

        // Using Coroutines to be able to download the image in a background thread. NOTE: To avoid UI blocking, Android doesn't allow us to download an image in the main thread.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val fileURL = URL("https://firebasestorage.googleapis.com/v0/b/rexmicrosystem.appspot.com/o/FileSharingApp%2Fanimal_5-7959AB57-594B-4473-8F0E-0FBBC30FC4F2.jpeg?alt=media&token=445b8041-5036-4740-bc42-d1b4e87104a7&_gl=1*kz2hb9*_ga*MjUzMDU0Mzg2LjE2ODkzNjkwNDg.*_ga_CW55HF8NVT*MTY5ODc3ODQyNS4xNDUuMS4xNjk4Nzc4NDM0LjUxLjAuMA..")
                var fileBitmap: Bitmap
                withContext(Dispatchers.IO) {// Launching a background job.
                    fileBitmap = BitmapFactory.decodeStream(fileURL.openStream()) // Downloading the image in a background task (coroutine)
                }
                imageView.setImageBitmap(fileBitmap)
                MaterialAlertDialogBuilder(this@ImageActivity)
                    .setTitle("Success")
                    .setMessage("Hi Jolly!")
                    .show()
            } catch (e: Exception){
                MaterialAlertDialogBuilder(this@ImageActivity)
                    .setTitle("Error")
                    .setMessage("Error while loading the image.")
                    .show()
                e.printStackTrace()
            }

        }
    }
}