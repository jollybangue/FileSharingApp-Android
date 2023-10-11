package com.rexmicrosystems.filesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Add a welcome alert or Snackbar in Home activity, triggered when the login is successful.
// TODO: Implement the "Sign Out" functionality.
class HomeActivity : AppCompatActivity() {

    private lateinit var homeAuth: FirebaseAuth
    private lateinit var textViewCurrentUser: TextView
    private lateinit var recyclerViewFileList: RecyclerView
    private lateinit var buttonSignOut: Button
    private lateinit var buttonUpload: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeAuth = Firebase.auth // Initialize the FirebaseAuth instance.
        textViewCurrentUser = findViewById(R.id.textViewCurrentUser)
        recyclerViewFileList = findViewById(R.id.recyclerViewFileList)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonUpload = findViewById(R.id.buttonUpload)

        textViewCurrentUser.text = homeAuth.currentUser?.email

        var fileList = mutableListOf(
            FileDetail("id001", "Jolly.rex"),
            FileDetail("id002", "BBB.pdf"),
            FileDetail("id003", "Video of my soccer goal.mp4"),
            FileDetail("id004", "Early bird.jpeg")
        )

        recyclerViewFileList.adapter = FileDetailAdapter(fileList)
        recyclerViewFileList.layoutManager = LinearLayoutManager(this)

        buttonUpload.setOnClickListener {
            // TODO: Upload feature to be implemented...
        }

        buttonSignOut.setOnClickListener {
            // TODO: Add a confirmation dialog
            homeAuth.signOut() // Add try...catch
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                Toast.makeText(this, "User logged out successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

