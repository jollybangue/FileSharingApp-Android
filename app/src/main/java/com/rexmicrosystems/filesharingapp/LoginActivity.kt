//
//  LoginActivity.kt
//  FileSharingApp
//
//  Created by Jolly BANGUE on 2023-09-28.
//
// Description: A native Android app that enables users to upload, download, delete, and share files using Firebase features (Firebase Authentication and Firebase Cloud Storage).

//TODO: Improve Login and Register Firebase Authentication implementation.
//  For example, when a user account is deleted in the Firebase Web Console,
//  the user stays connected on the local device and the Home activity is automatically loaded when we launch the app (the problem seems to be fixed when we uninstall the app...).
// Also, try to use fragments to navigate indefinitely between Login and Register screens (in that way there will be no need the destroy the previous activity while moving to a new activity).



package com.rexmicrosystems.filesharingapp

import android.annotation.SuppressLint // Automatically added after adding the expression "hideKeyboard(it)"
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.internal.ViewUtils.hideKeyboard // Automatically added after adding the expression "hideKeyboard(it)"
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.appAuth
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.userEmail
// "*.Companion.*" are static variables. These are the fields of the companion object defined in HomeActivity.

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextLoginEmail: EditText
    private lateinit var editTextLoginPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var buttonGoToRegister: Button

    @SuppressLint("RestrictedApi") // Automatically added after adding the expression "hideKeyboard(it)"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        appAuth = Firebase.auth // Initialize the FirebaseAuth instance.

        editTextLoginEmail = findViewById(R.id.editTextLoginEmail)
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonGoToRegister = findViewById(R.id.buttonGoToRegister)

        buttonSignIn.setOnClickListener {
            hideKeyboard(it) // Hide keyboard after clicking on "Sign In" button.
            if (editTextLoginEmail.text.toString().isEmpty() || editTextLoginPassword.text.toString().isEmpty()) {
                Snackbar.make(it, "Sorry, the Email or Password text field is empty.", Snackbar.LENGTH_SHORT).show()
            } else {
                val email: String = editTextLoginEmail.text.toString()
                val password: String = editTextLoginPassword.text.toString()

                appAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        userEmail = appAuth.currentUser?.email

                        Intent(this, HomeActivity::class.java).also { it ->
                            startActivity(it) // Loads HomeActivity
                            finish() // Destroys LoginActivity in the background
                        }
                    } else {
                        // "val resultMessage = task.exception?.message" can also be used
                        val resultLocalizedMessage = task.exception?.localizedMessage
                        Snackbar.make(it, resultLocalizedMessage.toString(), 6000).show()
                    // Duration: 6000 ms or 6 seconds. We can also use "resultLocalizedMessage!!" to unwrap the value (to be used ONLY if we are 100% sure that resultLocalizedMessage is non null).

                    }
                }
            }
        }

        buttonGoToRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it) // Launches activity RegisterActivity
                finish() // Destroys current activity (LoginActivity)
            }
        }
    }

    public override fun onStart() {
        super.onStart()

        // TODO: Check if the lines below can be implemented in the onCreate() function instead
        // When initializing this Activity (LoginActivity), checks to see if a user is currently signed in.
        if (appAuth.currentUser != null) {
            // Checks if user is signed in (non-null) and updates UI accordingly.
            userEmail = appAuth.currentUser?.email

            Intent(this, HomeActivity::class.java).also { it ->
                startActivity(it) // Launches activity HomeActivity
                finish() // Destroys current activity (LoginActivity)
            }
        }
    }
}