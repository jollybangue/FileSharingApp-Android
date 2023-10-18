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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import java.util.TreeMap

// TODO: Improve alert dialog management
// TODO: Generate app documentation
// TODO: URGENT - When a new iOS/Android user log in, the home screen blinks/flashes (because of the refreshment of the Realtime Database), the screen (recycler view) is entirely refreshed and the list is repositioned at the top (the top files appear...)
// TODO: Sort the data got from the realtime database
// TODO: Implement "File Details", "Delete File", "Upload File", "Download File", "Open File", "Share"
// TODO: Implement Swipe to delete file
// TODO: Check if the error while connecting to the Firebase cloud storage is handled.



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

    var fileDetailList: MutableList<FileDetail> = mutableListOf() // NOTE: Difference between array and mutable list: An array has a fixed size, whereas the size of a mutable list can increase or decrease dynamically.


    private val myStorageRef = Firebase.storage.reference // Pointing to the Firebase Cloud Storage root folder. iOS Swift: private let myStorageRef = Storage.storage().reference()
    private val fileStorageRoot = "FileSharingApp" // Root folder of the app data in the Firebase Cloud Storage.

    private val realtimeDbRef = Firebase.database.reference // Pointing to the Firebase Realtime Database root node (It is the Realtime database reference). iOS Swift: private let realtimeDbRef = Database.database().reference()
    private val realtimeDbRoot = "FileSharingApp" // Root folder of the app data in the Realtime database.

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
//        fileDetailList = mutableListOf(
//            FileDetail("id001", "Jolly.rex"),
//            FileDetail("id002", "BBB.pdf"),
//            FileDetail("id003", "Video of my soccer goal.mp4"),
//            FileDetail("id004", "Early bird.jpeg"),
//            FileDetail("id007", "Linkin Park - What I've Done.mp3"),
//            FileDetail("00100", "Metal Gear Solid V - The Phantom Pain.ps4")
//        )

        recyclerViewFileList.layoutManager = LinearLayoutManager(this)
        recyclerViewFileList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) // Adding a divider to separate the items shown in the Recycler view.


        copyDataFromStorageToRealtimeDB() // Getting the list of files available in the Firebase Cloud Storage and storing them in the Realtime Database.
        getFileNamesFromRealtimeDB() // Get and observe the file list stored in the Realtime Database.


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
                .setMessage("${userEmail}\n\nDo you want to sign out?")
                .setCancelable(false) // The dialog will be cancelled ONLY when the user choose an action inside the dialog, NOT by clicking outside the dialog.
                .setNegativeButton("CANCEL") { _, _ ->
                    Toast.makeText(this, "You cancelled sign out", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("SIGN OUT") { _, _ ->
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
     * the Realtime database.
     */
    private fun copyDataFromStorageToRealtimeDB() {
        myStorageRef.child(fileStorageRoot).listAll()
            .addOnSuccessListener { (items, prefixes) ->

                realtimeDbRef.child(realtimeDbRoot).removeValue() // Deletes all the current values in realtime database app folder to avoid duplication issues.

//                for (prefix in prefixes) { // List of folder storage references.
//                    // All the prefixes (folders) under fileStorageRoot.
//                    // We may call listAll() recursively on them.
//                    // TODO: Implement folder management
//                }

                var id = 1000 // Initializing the file id which will be used to store the file in the Realtime database. With id = 10, we have ids from 10 to 99; With id = 100, we have ids from 100 to 999; With id = 1000, we have ids from 1000 to 9999.
                for (item in items) { // List of file storage references. All the items (files) under fileStorageRoot.

                    realtimeDbRef.child(realtimeDbRoot).child("id$id").setValue(item.name) // Writing file names gotten from Firebase cloud storage into Firebase Realtime database, with ids generated manually. Min: id1000, Max: id9999, Total: 9000 potential ids.

//                    fileDetailList.add(FileDetail("id$id", item.name))
//                    println("FileDetailList INSIDE listAll(): $fileDetailList")

                    id += 1 // Incrementing the id.
                }
//                recyclerViewFileList.adapter = FileDetailAdapter(fileDetailList)

            }
            .addOnFailureListener {
                showAlertDialog("Error while getting the list of the files stored in the cloud", "Details: ${it.message}")
            }
    }


    /**
     * This function allows the app to get and observe in realtime, the name of the files stored in
     * the Firebase cloud storage.
     */
    private fun getFileNamesFromRealtimeDB() {

        // Defining listener which will be passed to addValueEventListener() below
        val realtimeFileListListener = object : ValueEventListener {
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param fileListSnapshot The current data at the location
             */
            override fun onDataChange(fileListSnapshot: DataSnapshot) {

                val snapshotFileNamesMap = fileListSnapshot.value as Map<*, *>? // Getting the value of fileListSnapshot and casting it as Map.

                fileDetailList = mutableListOf()

                if (snapshotFileNamesMap != null) { // Unwrapping snapshotFileNamesMap
                    // Here we need to sort the map snapshotFileNamesMap by keys natural order
                    // There are many ways to proceed. Please see: https://www.techiedelight.com/sort-map-by-keys-kotlin/

                    // I chose to convert snapshotFileNamesMap into a TreeMap. In a TreeMap, the elements are always sorted by keys' natural ordering.
                    val sortedMap = TreeMap(snapshotFileNamesMap)

                    for ((key, value) in sortedMap) {
                        fileDetailList.add(FileDetail(key as String, value as String)) // Casting sortedMap's keys and values of type Any? to String.
                    }

                }
                recyclerViewFileList.adapter = FileDetailAdapter(fileDetailList) // Passing fileDetailList to the recycler view via the adapter. NOTE: fileDetailList is NOT accessible outside onDataChange() function.
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase Database rules. For more information on
             * securing your data, see: [ Security Quickstart](https://firebase.google.com/docs/database/security/quickstart)
             *
             * @param error A description of the error that occurred
             */
            override fun onCancelled(error: DatabaseError) {
                showAlertDialog("Realtime Database Error", "Details: ${error.message}")
            }

        }

        // Adding a listener to check any data change which could occur at the realtime database root of this app.
        realtimeDbRef.child(realtimeDbRoot).addValueEventListener(realtimeFileListListener)
    }

    fun showAlertDialog (title: String, message: String) {
        val myAlertDialog = MaterialAlertDialogBuilder(this)
        myAlertDialog
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton("OK") {_, _ ->}
            .show()
    }
}
