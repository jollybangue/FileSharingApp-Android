//
//  LoginActivity.kt
//  FileSharingApp
//
//  Created by Jolly BANGUE on 2023-09-28.
//
// Description: A native Android app that enables users to upload, download, delete, and share files using Firebase features (Firebase Authentication and Firebase Cloud Storage).

package com.rexmicrosystems.filesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextLoginEmail = findViewById<EditText>(R.id.editTextLoginEmail)
        val editTextLoginPassword = findViewById<EditText>(R.id.editTextLoginPassword)
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)
        val buttonGoToRegister = findViewById<Button>(R.id.buttonGoToRegister)

        buttonGoToRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}