//
//  LoginActivity.kt
//  FileSharingApp
//
//  Created by Jolly BANGUE on 2023-09-28.
//
// Description: A native Android app that enables users to upload, download, delete, and share files using Firebase features (Firebase Authentication and Firebase Cloud Storage).

package com.rexmicrosystems.filesharingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}