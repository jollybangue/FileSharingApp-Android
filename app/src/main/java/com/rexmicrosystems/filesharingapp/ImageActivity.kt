package com.rexmicrosystems.filesharingapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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

        // Download and set the selected image in Image View...

        // Method 1: Using downloadUrl and Coroutines. downloadUrl allows us to download the file with our own code or using another library (different from Firebase)
        myStorageRef.child(fileStorageRoot).child(fileName!!).downloadUrl
            .addOnSuccessListener { myURI ->
            // Using Coroutines to be able to download the image in a background thread. NOTE: To avoid UI blocking, Android doesn't allow us to download an image in the main thread.
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val fileURL = URL(myURI.toString()) // Convert the URI to String and then to URL and save it into fileURL
                        var fileBitmap: Bitmap
                        withContext(Dispatchers.IO) {// Launching a background job.
                            fileBitmap = BitmapFactory.decodeStream(fileURL.openStream()) // Downloading the image in a background task (coroutine) and save it in fileBitmap
                            // Note: We can use either .openStream() or .openConnection().getInputStream() to read from a URL. However, reading from a URLConnection instead of reading directly from a URL might be more useful. This is because you can use the URLConnection object for other tasks (like writing to the URL) at the same time.
                            // Reference: https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
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
        // TODO: Implement a progression bar.

         //Method 2: Using Firebase library, with getBytes. NOTE: With this method, no exception is thrown when a NON-IMAGE file is selected for opening in Image View.
//        myStorageRef.child(fileStorageRoot).child(fileName!!).getBytes(20 * 1024 * 1024) // The max size of the downloadable file is set to 20 MB.
//            .addOnSuccessListener { dataByteArray ->
//                val fileData = BitmapFactory.decodeByteArray(dataByteArray, 0, dataByteArray.size) // Offset = 0. The offset is the starting address, of the byte where the bitmap image data (pixel array) can be found.
//                imageView.setImageBitmap(fileData)
//            }
//            .addOnFailureListener {
//                showAlertDialog("Image View Error", it.localizedMessage!!, this@ImageActivity)
//        }


    }
}