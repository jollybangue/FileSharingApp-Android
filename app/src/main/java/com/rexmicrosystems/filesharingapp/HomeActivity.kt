package com.rexmicrosystems.filesharingapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

    companion object {
        // We cannot create static variables in Kotlin. Instead, we should use a companion object
        // All the fields of a companion object are static and therefore accessible from everywhere in the app.
        var isNewUser: Boolean = false
        // TODO: Add all the Firebase Authentication fields here in the companion object.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (isNewUser) {
            // isNewUser becomes true ONLY when the new user account is successfully created (when the "Create Account" button is tapped).
            MaterialAlertDialogBuilder(this)
                // Note: With AlertDialog.Builder(this), while showing the dialog is less animated than with android.app.AlertDialog.Builder(this).
                .setTitle("Account created")
                .setMessage("Welcome Jolly Mambou Bangue!")
                .show()
            isNewUser = false // Once created the user is no longer new...
        }

        homeAuth = Firebase.auth // Initialize the FirebaseAuth instance.
        textViewCurrentUser = findViewById(R.id.textViewCurrentUser)
        recyclerViewFileList = findViewById(R.id.recyclerViewFileList)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonUpload = findViewById(R.id.buttonUpload)

        textViewCurrentUser.text = homeAuth.currentUser?.email

        var fileList = mutableListOf(
            FileDetail("id001", "Jolly.rex"),
            FileDetail("id002", "BBB.pdf"),
            FileDetail("id003", "Video of my soccer goal.mp4"),
            FileDetail("id004", "Early bird.jpeg"),
            FileDetail("id007", "Linkin Park - What I've Done.mp3")
        )


        var myAlertDialog = MaterialAlertDialogBuilder(this)


        val actionItem = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        recyclerViewFileList.adapter = FileDetailAdapter(fileList)
        recyclerViewFileList.layoutManager = LinearLayoutManager(this)
        recyclerViewFileList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) // Adding a divider to separate the items shown in the Recycler view.
//        recyclerViewFileList.setOnClickListener {
//            Toast.makeText(this, "Recycler View touched", Toast.LENGTH_SHORT).show()
//        }

        buttonUpload.setOnClickListener {
            // TODO: Upload feature to be implemented...
            myAlertDialog.setTitle("Upload file")
            //myAlertDialog.setMessage("Hello Jolly. This is your first alert message!")
            myAlertDialog.setPositiveButton("Positive 1") {_, _ ->
            }

            //myAlertDialog.create() // It seems that there is no need to invoke create() when show() will be invoked right after.


            myAlertDialog.setItems(actionItem) { dialog, i ->
                Toast.makeText(this, "You selected ${actionItem[i]} and i = $i", Toast.LENGTH_LONG).show()
                println("Content of dialog local variable: $dialog")
                println("Content of i local variable: $i")
            }



            myAlertDialog.show()

        }

        buttonSignOut.setOnClickListener {
            // TODO: Add a confirmation dialog
            myAlertDialog.setTitle("Confirmation")
                .setMessage("Do you want to sign out?")
                .setNegativeButton("Cancel") { _, _ ->

                }
                .setPositiveButton("Sign Out") { _, _ ->
                    homeAuth.signOut() // Add try...catch
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                        finish()
                        Toast.makeText(this, "User logged out successfully", Toast.LENGTH_SHORT).show()
                    }
                    // Here we need to destroy the Home activity. In that way, when the back button will be pushed after Signing out we won't be able to back to the Home screen.

                }
                .show()

        }
    }
}

