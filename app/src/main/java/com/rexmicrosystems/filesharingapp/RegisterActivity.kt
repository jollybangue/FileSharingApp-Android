package com.rexmicrosystems.filesharingapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {




    private lateinit var registerAuth: FirebaseAuth

    private lateinit var editTextRegisterEmail: EditText
    private lateinit var editTextRegisterPassword: EditText
    private lateinit var buttonCreateAccount: Button
    private lateinit var buttonGoToLogin: Button

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerAuth = Firebase.auth // Initialize the FirebaseAuth instance.

        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail)
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount)
        buttonGoToLogin = findViewById(R.id.buttonGoToLogin)

        buttonCreateAccount.setOnClickListener {
            hideKeyboard(it) // Hide keyboard after clicking on "Create account" button.
            if (editTextRegisterEmail.text.toString().isEmpty() || editTextRegisterPassword.text.toString().isEmpty()) {
                //Toast.makeText(this, "Sorry, the Email or Password text field is empty...", Toast.LENGTH_SHORT).show()
                Snackbar.make(it, "Sorry, the Email or Password text field is empty.", Snackbar.LENGTH_SHORT).show()
            } else {
                val email = editTextRegisterEmail.text.toString()
                val password = editTextRegisterPassword.text.toString()

                registerAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val myNewUserEmail = registerAuth.currentUser?.email
                        HomeActivity.isNewUser = true
                        Intent(this, HomeActivity::class.java).also { it2 ->
                            startActivity(it2)
                            finish()

                            // TODO: Create an Alert to confirm the registration and to welcome the new user.
                            //Toast.makeText(this, "Account created successfully. Welcome $myNewUserEmail.", Toast.LENGTH_LONG).show()

                        }

                        //clearEditTextsRegister() // This is not necessary anymore, since the activity has been destroyed...
                    } else {
                        //val resultMessage = task.exception?.message
                        val resultLocalizedMessage = task.exception?.localizedMessage
                        Snackbar.make(it, resultLocalizedMessage.toString(), 6000).show()
                        // Duration: 6000 ms. We can also use "resultLocalizedMessage!!" to unwrap the value (to be used ONLY if we are 100% sure that resultLocalizedMessage is non null).

                    }
                }
            }
        }

        buttonGoToLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
            clearEditTextsRegister()
        }
    }

    private fun clearEditTextsRegister() {
        editTextRegisterEmail.text.clear()
        editTextRegisterPassword.text.clear()
    }
}