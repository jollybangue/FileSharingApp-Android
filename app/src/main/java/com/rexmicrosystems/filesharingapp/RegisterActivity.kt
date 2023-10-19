package com.rexmicrosystems.filesharingapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.appAuth
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.isNewUser
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.userEmail
// "*.Companion.*" are static variables. These are the fields of the companion object defined in HomeActivity.

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextRegisterEmail: EditText
    private lateinit var editTextRegisterPassword: EditText
    private lateinit var buttonCreateAccount: Button
    private lateinit var buttonGoToLogin: Button

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        appAuth = Firebase.auth // Initialize the FirebaseAuth instance. "appAuth" is a static variable belonging to the companion object defined in HomeActivity.

        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail)
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount)
        buttonGoToLogin = findViewById(R.id.buttonGoToLogin)

        buttonCreateAccount.setOnClickListener {
            hideKeyboard(it) // Hide keyboard after clicking on the "Create Account" button.
            if (editTextRegisterEmail.text.toString().isEmpty() || editTextRegisterPassword.text.toString().isEmpty()) {
                Snackbar.make(it, "Sorry, the Email or Password text field is empty.", Snackbar.LENGTH_SHORT).show()
            } else {
                val email = editTextRegisterEmail.text.toString()
                val password = editTextRegisterPassword.text.toString()

                //"appAuth" is a static variable belonging to the companion object defined in HomeActivity.
                appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        userEmail = appAuth.currentUser?.email // As a rule, I decided that "userEmail" will be assigned a value ONLY from LoginActivity and RegisterActivity, and so NEVER from HomeActivity.

                        isNewUser = true // Setting the static boolean variable "isNewUser" (belonging to the companion object defined in HomeActivity) to true because a new user account has been successfully created.
                        // When "isNewUser" is true, HomeActivity shows a welcome alert dialog to the new created user.

                        Intent(this, HomeActivity::class.java).also { it ->
                            startActivity(it) // Starts HomeActivity
                            finish() // Destroys the current activity (RegisterActivity)
                        }

                    } else {
                        // val resultMessage = task.exception?.message // This expression can also be used.
                        val resultLocalizedMessage = task.exception?.localizedMessage
                        Snackbar.make(it, "Failed to create an account.\n${resultLocalizedMessage.toString()}", 6000).show()
                        // Duration: 6000 ms or 6 seconds. We can also use "resultLocalizedMessage!!" to unwrap the value ( but to be used ONLY if we are 100% sure that resultLocalizedMessage is non null).
                    }
                }
            }
        }

        buttonGoToLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it) // Go to LoginActivity
                finish() // Destroys current activity (RegisterActivity)
            }
        }
    }
}