package com.rexmicrosystems.filesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth


// TODO: Improve alert dialog management

class HomeActivity : AppCompatActivity() {

    companion object {
        // We cannot create static variables in Kotlin. Instead, we should use a companion object.
        // All the fields of a companion object are static and therefore accessible from everywhere in the app.
        lateinit var appAuth: FirebaseAuth
        var isNewUser: Boolean = false // isNewUser is set to true when a new user account is successfully created in RegisterActivity.
        var userEmail: String? = null // userEmail = appAuth.currentUser?.email when the user account is successfully created (see RegisterActivity).
        // As a rule, I decided that "userEmail" and all the companion fields (except "isNewUser") will be assigned a value ONLY from LoginActivity and RegisterActivity, and so NEVER from HomeActivity.
    }

    private lateinit var textViewCurrentUser: TextView
    private lateinit var recyclerViewFileList: RecyclerView
    private lateinit var buttonSignOut: Button
    private lateinit var buttonUpload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (isNewUser) {
            // isNewUser becomes true ONLY when the new user account is successfully created (when the "Create Account" button is tapped).
            MaterialAlertDialogBuilder(this)
                // Note: With AlertDialog.Builder(this), while showing the dialog is less animated than with android.app.AlertDialog.Builder(this).
                .setTitle("Account successfully created")
                .setMessage("Welcome $userEmail.\nWe hope you will enjoy this File Sharing App.")
                .show()
            isNewUser = false // Once created the user is no longer new...
        }

        textViewCurrentUser = findViewById(R.id.textViewCurrentUser)
        recyclerViewFileList = findViewById(R.id.recyclerViewFileList)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonUpload = findViewById(R.id.buttonUpload)

        textViewCurrentUser.text = userEmail // Setting the text of the Home label with the email of the currently logged user.

        // Data source...
        var fileList = mutableListOf(
            FileDetail("id001", "Jolly.rex"),
            FileDetail("id002", "BBB.pdf"),
            FileDetail("id003", "Video of my soccer goal.mp4"),
            FileDetail("id004", "Early bird.jpeg"),
            FileDetail("id007", "Linkin Park - What I've Done.mp3")
        )

        val uploadActionItem = arrayOf("Upload from Gallery", "Upload from Another Location")

        recyclerViewFileList.adapter = FileDetailAdapter(fileList)
        recyclerViewFileList.layoutManager = LinearLayoutManager(this)
        recyclerViewFileList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) // Adding a divider to separate the items shown in the Recycler view.

        buttonUpload.setOnClickListener {
            // TODO: Upload feature to be implemented...
            MaterialAlertDialogBuilder(this)
                .setTitle("Upload a File")
                .setPositiveButton("CANCEL") {_, _ ->
                    Toast.makeText(this, "Upload cancelled", Toast.LENGTH_SHORT).show()
                }

                .setItems(uploadActionItem) { dialog, i ->
                    Toast.makeText(this, "You selected ${uploadActionItem[i]}", Toast.LENGTH_SHORT).show()

                    println("Content of dialog local variable: $dialog")
                    println("Content of i local variable: $i")
                }
                .show() // It seems that there is no need to invoke create() when show() will be invoked right after.
        }

        buttonSignOut.setOnClickListener {
            // TODO: Add a confirmation dialog
            MaterialAlertDialogBuilder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to sign out?")
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(this, "Cancelled sign-out", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Sign Out") { _, _ ->
                    appAuth.signOut() // Add try...catch
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it) // Start LoginActivity
                        finish() // Destroys the Home activity. In that way, when the back button will be pushed after Signing out we won't be able to back to the Home screen.
                        Toast.makeText(this, "User $userEmail logged out successfully", Toast.LENGTH_SHORT).show()
                    }

                }
                .show()
        }
    }
}

