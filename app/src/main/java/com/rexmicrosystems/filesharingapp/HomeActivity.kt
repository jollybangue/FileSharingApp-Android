package com.rexmicrosystems.filesharingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var homeAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeAuth = Firebase.auth // Initialize the FirebaseAuth instance.

        val textViewCurrentUser: TextView = findViewById(R.id.textViewCurrentUser)
        textViewCurrentUser.text = homeAuth.currentUser?.email
    }
}