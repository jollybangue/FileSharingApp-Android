package com.rexmicrosystems.filesharingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Add a welcome alert or Snackbar in Home activity, triggered when the login is successful.
// TODO: Implement the "Sign Out" functionality.
class HomeActivity : AppCompatActivity() {

    private lateinit var homeAuth: FirebaseAuth

    private lateinit var textViewCurrentUser: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeAuth = Firebase.auth // Initialize the FirebaseAuth instance.

        textViewCurrentUser = findViewById(R.id.textViewCurrentUser)
        textViewCurrentUser.text = homeAuth.currentUser?.email
    }
}