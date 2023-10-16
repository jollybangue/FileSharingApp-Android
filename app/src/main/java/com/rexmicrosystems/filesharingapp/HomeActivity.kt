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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

// TODO: Improve alert dialog management
// TODO: Generate app documentation

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
    private lateinit var buttonUpload: Button
    private lateinit var buttonSignOut: Button


    private val myStorageRef = Firebase.storage.reference // Pointing to the Firebase Cloud Storage root folder. iOS Swift: private let myStorageRef = Storage.storage().reference()
    private val fileStorageRoot = "FileSharingApp" // Root folder of the app data in the Firebase Cloud Storage.

    private val realtimeDbRef = Firebase.database.reference // Pointing to the Firebase Realtime Database root node (It is the Realtime database reference). iOS Swift: private let realtimeDbRef = Database.database().reference()
    private val realtimeDbRoot = "FileSharingApp" // Root folder of the app data in the Realtime database.

    //private lateinit var realtimeFileList: MutableList<FileDetail> // NOTE: Difference between array and mutable list: An array has a fixed size, whereas the size of a mutable list can increase or decrease dynamically.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Checking if the app should show a welcome AlertDialog to the newly created user.
        if (isNewUser) {
            // isNewUser becomes true ONLY when the new user account is successfully created (when the "Create Account" button is tapped, in the RegisterActivity).
            MaterialAlertDialogBuilder(this)
                // Note: With AlertDialog.Builder(this), the dialog is less animated than with android.app.AlertDialog.Builder(this). We use MaterialAlertDialogBuilder(this) for the new Material3 design.
                .setTitle("Account successfully created.")
                .setMessage("Welcome $userEmail.\nWe hope you will enjoy this File Sharing App!")
                .show()
            isNewUser = false // Once created the user is no longer new...
        }

        textViewCurrentUser = findViewById(R.id.textViewCurrentUser)
        recyclerViewFileList = findViewById(R.id.recyclerViewFileList)
        buttonUpload = findViewById(R.id.buttonUpload)
        buttonSignOut = findViewById(R.id.buttonSignOut)


        textViewCurrentUser.text = userEmail // Setting the text of the Home label with the email of the currently logged user.

        // Data source, containing a list of FileDetail objects, which will be displayed in the recycler view.
        var fileDetailList = mutableListOf(
            FileDetail("id001", "Jolly.rex"),
            FileDetail("id002", "BBB.pdf"),
            FileDetail("id003", "Video of my soccer goal.mp4"),
            FileDetail("id004", "Early bird.jpeg"),
            FileDetail("id007", "Linkin Park - What I've Done.mp3"),
            FileDetail("00100", "Metal Gear Solid V - The Phantom Pain.ps4")
        )

        copyDataFromStorageToRealtimeDB() // Getting the list of files available in the Firebase Cloud Storage and storing them in the Realtime Database.
        getFileNamesFromRealtimeDB() // Get and observe the file list stored in the Realtime Database.

        recyclerViewFileList.adapter = FileDetailAdapter(fileDetailList)
        recyclerViewFileList.layoutManager = LinearLayoutManager(this)
        recyclerViewFileList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) // Adding a divider to separate the items shown in the Recycler view.

        buttonUpload.setOnClickListener {
            // TODO: Upload feature to be implemented...
            val uploadActionItem = arrayOf("Upload from Gallery", "Upload from Another Location") // TODO: Check if it is better to just use buttons instead, NOT this list of 2 elements.

            val uploadDialog = MaterialAlertDialogBuilder(this)
            uploadDialog
                .setTitle("Upload a File")
                .setCancelable(false)

                .setPositiveButton("CANCEL") {_, _ ->
                    Toast.makeText(this, "Upload cancelled", Toast.LENGTH_SHORT).show()
                }

                .setItems(uploadActionItem) { dialog, i ->
                    Toast.makeText(this, "You selected ${uploadActionItem[i]}", Toast.LENGTH_SHORT).show()

                    println("Content of dialog local variable: $dialog")
                    println("Content of i local variable: $i")
                }
                .show() // It seems that there is no need to invoke create() when show() will be invoked right after (because show() = create() + show()).
        }

        buttonSignOut.setOnClickListener {
            val signOutDialog = MaterialAlertDialogBuilder(this)
            signOutDialog
                .setTitle("Confirmation")
                .setMessage("Do you want to sign out?")
                .setCancelable(false) // The dialog will be cancelled ONLY when the user choose an action inside the dialog, NOT by clicking outside the dialog.
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(this, "Cancelled sign-out", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Sign Out") { _, _ ->
                    appAuth.signOut() // TODO: Add try...catch
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it) // Start LoginActivity
                        finish() // Destroys the Home activity. In that way, when the back button will be pushed after Signing out we won't be able to back to the Home screen.
                        Toast.makeText(this, "User $userEmail successfully logged out", Toast.LENGTH_LONG).show()
                    }
                }
                .show() // Showing the Sign Out Dialog
        }
    }


    /**
     * This function gets the list of files stored in the Firebase cloud storage and save it into
     * the Realtime database using the setFileNamesInRealtimeDB() function.
     */
    fun copyDataFromStorageToRealtimeDB() {
//        myStorageRef.child(fileStorageRoot).listAll()
//            .addOnSuccessListener { (items, prefixes) ->
//
//                realtimeDbRef.child(realtimeDbRoot).removeValue() // Deletes all the current values in realtime database app folder to avoid duplication issues.
//
//                for (prefix in prefixes) { // List of folder storage references.
//                    // TODO: Implement folder management
//                }
//
//                for (item in items) { // List of file storage references.
//                    realtimeDbRef.child(realtimeDbRoot).
//                }
//            }


    }

    /**
     * This This function takes a list of fileReferences (array of StorageReference objects)
     * from the Firebase Cloud Storage, then extracts and stores the name of each file in the
     * Realtime database, with an associate unique ID.
     *
     * NOTE: This function cannot be directly called by the app, but ONLY by the function
     * copyDataFromStorageToRealtimeDB() and can be ONLY used INSIDE the "listAll" function.
     */
    fun setFileNamesInRealtimeDB() {

    }

    /**
     * This function allows the app to get and observe in realtime, the name of the files stored in
     * the Firebase cloud storage.
     */
    fun getFileNamesFromRealtimeDB() {
        //lateinit var fileList: MutableList<FileDetail>



        //return fileList
    }
}

