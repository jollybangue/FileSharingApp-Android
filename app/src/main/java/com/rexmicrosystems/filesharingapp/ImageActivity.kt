package com.rexmicrosystems.filesharingapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.myContext
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.showAlertDialog
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val imageView = findViewById<ImageView>(R.id.imageView)

        var fileName = intent.getStringExtra("EXTRA_FILENAME")
        supportActionBar?.title = fileName
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Adding a back to Home button in the left side of the AppBar. To make the back button functional, I added "android:parentActivityName=".HomeActivity"" in the AndroidManifest.xml file, at the section of ImageActivity.

        val executor = Executors.newSingleThreadExecutor()

        val handler = Handler(Looper.getMainLooper())

        executor.execute {

            try {
                val fileURL = URL("https://firebasestorage.googleapis.com/v0/b/rexmicrosystems.appspot.com/o/FileSharingApp%2FFile%203.jpg?alt=media&token=d27439f5-916f-4698-ae72-21122369939e&_gl=1*145w37n*_ga*MjUzMDU0Mzg2LjE2ODkzNjkwNDg.*_ga_CW55HF8NVT*MTY5ODY0MDI5My4xNDQuMS4xNjk4NjQwOTg3LjU1LjAuMA..")
                val fileBitmap = BitmapFactory.decodeStream(fileURL.openStream())//.getInputStream())
                //val fileBitmap = BitmapFactory.decodeStream(fileURL.openConnection().getInputStream())//.getInputStream())

                handler.post {
                    imageView.setImageBitmap(fileBitmap)
                }
            }
            catch (e: Exception) {

                runOnUiThread {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Error Loading Image")
                        .setMessage(e.message)
                        .show()
                    //Toast.makeText(this, "Error Loading Image...", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
                println("Error Message: ${e.localizedMessage}")
            }
            
        }

    }
}