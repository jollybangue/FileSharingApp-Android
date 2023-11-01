package com.rexmicrosystems.filesharingapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.fileStorageRoot
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.myStorageRef
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.showAlertDialog
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

        val fileName = intent.getStringExtra("EXTRA_FILENAME")
        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Adding a back to Home button in the left side of the AppBar. To make the back button functional, I added "android:parentActivityName=".HomeActivity"" in the AndroidManifest.xml file, at the section of ImageActivity.

        // Downloading URL of the selected file...
        myStorageRef.child(fileStorageRoot).child(fileName!!).downloadUrl
            .addOnSuccessListener { myURI ->
            // Using Coroutines to be able to download the image in a background thread. NOTE: To avoid UI blocking, Android doesn't allow us to download an image in the main thread.
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val fileURL = URL(myURI.toString()) // Convert the URI to String and then to URL and save it into fileURL
                        var fileBitmap: Bitmap
                        withContext(Dispatchers.IO) {// Launching a background job.
                            fileBitmap = BitmapFactory.decodeStream(fileURL.openStream()) // Downloading the image in a background task (coroutine) and save it in fileBitmap
                        }
                        imageView.setImageBitmap(fileBitmap)

                    } catch (e: Exception){
                        showAlertDialog("Error", "Unable to load the file \"$fileName\" in Image View.", this@ImageActivity)
                        e.printStackTrace()
                    }
                }
            }
            .addOnFailureListener {
                showAlertDialog("URL Download Error", it.localizedMessage!!, this@ImageActivity)
            }
    }
}