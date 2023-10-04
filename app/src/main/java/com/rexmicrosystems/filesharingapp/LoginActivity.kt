//
//  LoginActivity.kt
//  FileSharingApp
//
//  Created by Jolly BANGUE on 2023-09-28.
//
// Description: A native Android app that enables users to upload, download, delete, and share files using Firebase features (Firebase Authentication and Firebase Cloud Storage).

package com.rexmicrosystems.filesharingapp

import android.annotation.SuppressLint // Automatically added after adding the expression "hideKeyboard(it)"
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.internal.ViewUtils.hideKeyboard // Automatically added after adding the expression "hideKeyboard(it)"
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var myAuth: FirebaseAuth
    @SuppressLint("RestrictedApi") // Automatically added after adding the expression "hideKeyboard(it)"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO: Disable the back button here.

        myAuth = Firebase.auth // Initialize the FirebaseAuth instance.

        val editTextLoginEmail = findViewById<EditText>(R.id.editTextLoginEmail)
        val editTextLoginPassword = findViewById<EditText>(R.id.editTextLoginPassword)
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)
        val buttonGoToRegister = findViewById<Button>(R.id.buttonGoToRegister)

        buttonSignIn.setOnClickListener {
            hideKeyboard(it) // Hide keyboard after clicking on "Sign In" button.
            if (editTextLoginEmail.text.toString().isEmpty() || editTextLoginPassword.text.toString().isEmpty()) {
                //Toast.makeText(this, "Sorry, the Email or Password text field is empty...", Toast.LENGTH_SHORT).show()
                Snackbar.make(it, "Sorry, the Email or Password text field is empty.", Snackbar.LENGTH_SHORT).show()
            } else {
                val email = editTextLoginEmail.text.toString()
                val password = editTextLoginPassword.text.toString()

                myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Intent(this, HomeActivity::class.java).also { it ->
                            startActivity(it)
                        }

                    } else {
                        //val resultMessage = task.exception?.message
                        val resultLocalizedMessage = task.exception?.localizedMessage
                        Snackbar.make(it, resultLocalizedMessage.toString(), 6000).show()
                    // Duration: 6000 ms. We can also use "resultLocalizedMessage!!" to unwrap the value (to be used ONLY if we are 100% sure that resultLocalizedMessage is non null).

                    }
                }
            }
        }

        buttonGoToRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // When initializing the Activity, check to see if the user is currently signed in.
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = myAuth.currentUser
        if (currentUser != null) {
            // reload()
            // Or load a new activity?
        }
    }

}