package com.rexmicrosystems.filesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        // I use mutableMapOf() instead of HashMap() because mutableMapOf() keeps the order of key/values put, which is not the case of HashMap().
        var fileMutableMap: MutableMap<String, String> = mutableMapOf<String, String>()
        fileMutableMap["key001"] = "File 1"
        fileMutableMap["key002"] = "File A"
        fileMutableMap["key003"] = "File B"
        fileMutableMap["key004"] = "Jolly"

        var fileMutableMap2: MutableMap<String, String> = mutableMapOf<String, String>()
        fileMutableMap2.put("Name", "Jolly")
        fileMutableMap2["City"] = "Calgary"
        fileMutableMap2.put("Nationality", "Cameroonian")
        fileMutableMap2["name"] = "MBJ"

        //fileMutableMap2.clear()
        //fileMutableMap2.putAll(fileMutableMap)

        buttonUpload.setOnClickListener {
            println("Content of fileMutableMap: $fileMutableMap")
            println("Content of fileMutableMap2: $fileMutableMap2")
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

