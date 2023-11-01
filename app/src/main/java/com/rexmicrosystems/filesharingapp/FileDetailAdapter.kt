//
//  FileDetailAdapter.kt
//  FileSharingApp
//
//  Created by Jolly Bangue on 2023-09-29.
//

// TODO: Set contentDescription attribute of the image in ImageActivity (activity_image.xml). I temporary set the property to "@string/app_name"

package com.rexmicrosystems.filesharingapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.copyDataFromStorageToRealtimeDB
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.fileStorageRoot
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.myStorageRef
import com.rexmicrosystems.filesharingapp.HomeActivity.Companion.showAlertDialog // import done to be able to call the static method showAlertDialog
import java.text.DecimalFormat
import java.util.Date

class FileDetailAdapter(private var fileList: List<FileDetail>): RecyclerView.Adapter<FileDetailAdapter.FileDetailViewHolder>() {
    // fileList is the parameter of the constructor



    class FileDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewFileName: TextView = itemView.findViewById(R.id.textViewFileName)
    }

    // Creating the view holder (FileDetailViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDetailViewHolder { // Should return FileDetailViewHolder type
        val fileDetailView = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return FileDetailViewHolder(fileDetailView) // fileDetailView is the view of one item of the recycler view. In our case, one item is a fileDetail object (a text view containing the name of a file stored in the Firebase Cloud Storage)
    }

    override fun getItemCount(): Int {
        return fileList.size // Returning the number of FileDetail objects stored in the list (fileList) used as data source for the recycler view.
    }

    override fun onBindViewHolder(holder: FileDetailViewHolder, position: Int) {

        val fileSelectedName = fileList[position].name

        holder.textViewFileName.text = fileSelectedName

        // List of actions shown when the user click on a file. Will be used to set items in the fileActionsDialog...
        val fileAction = arrayOf("Open in Image View", "Open in Web View", "Open with System", "Download in the Default App Folder", "Download in Specified Location", "Delete File")

        holder.itemView.setOnClickListener { myItemView ->
            // VERY IMPORTANT NOTE: In "file_item.xml", android:foreground="?selectableItemBackground" allows to highlight the selected item.
            // Do I need  android:clickable="true" and android:focusable="true" ??? It seems NO!!!.

            //notifyItemChanged(position) // TODO: Check if this expression is necessary...

            // Creating fileActionsDialog which will be shown when the user clicks on a file. NOTE: Unable to define fileActionsDialog outside .setOnClickListener{}...
            val fileActionsDialog = MaterialAlertDialogBuilder(myItemView.context)
            fileActionsDialog
                .setTitle(fileSelectedName)
                .setCancelable(false) // The user should select an item or a button to dismiss the alert dialog.

                .setPositiveButton("SHARE") {_, _ ->
                    myStorageRef.child(fileStorageRoot).child(fileSelectedName).downloadUrl.addOnSuccessListener { myUri ->
                        val myClipboard = getSystemService(myItemView.context, ClipboardManager::class.java) // Getting the system clipboard
                        val myClipData = ClipData.newUri(myItemView.context.contentResolver, "URI", myUri) // Converting the URL contained in myUri as a ClipData
                        myClipboard?.setPrimaryClip(myClipData) // Putting the clip data into the clipboard.
                        showAlertDialog(fileSelectedName, "File link copied to clipboard", myItemView.context)
                        println("File link to share: $myUri")
                    }
                        .addOnFailureListener { error ->
                            showAlertDialog("Share Error", error.localizedMessage!!, myItemView.context)
                        }
                }

                .setNegativeButton("CANCEL") {_, _ ->
                    // No action needed here. Just dismiss the Alert Dialog
                }

                .setNeutralButton("FILE DETAILS") {_, _ ->
                    // Getting the file (located in Cloud Storage) metadata
                    myStorageRef.child(fileStorageRoot).child(fileSelectedName).metadata.addOnSuccessListener { metadata ->
                        val name = metadata.name
                        val fileKind = metadata.contentType
                        val fileSize = DecimalFormat("#,###").format(metadata.sizeBytes) // metadata.sizeBytes is the size (in bytes) of the selected file. DecimalFormat with pattern "#,###" adds comma separators in the value of the file size.
                        val fileDateCreated = Date(metadata.creationTimeMillis) // Converting the Timestamp metadata.creationTimeMillis to Date format
                        val fileDateModified = Date(metadata.updatedTimeMillis)
                        showAlertDialog("File Details", "Name: $name\n\nKind: $fileKind file\n\nSize: $fileSize bytes\n\nCreated: $fileDateCreated\n\nModified: $fileDateModified", myItemView.context)
                    }
                    .addOnFailureListener { error ->
                        showAlertDialog("Metadata Error", error.localizedMessage!!, myItemView.context)
                    }
                }

                .setItems(fileAction) { _, i -> // (DialogInterface, Int)
                    //Toast.makeText(it.context, "You selected \"${fileAction[i]}\" action", Toast.LENGTH_SHORT).show()

                    when (fileAction[i]) {
                        "Open in Image View" -> {
                            val imageViewIntent = Intent(myItemView.context, ImageActivity::class.java)
                            imageViewIntent.putExtra("EXTRA_FILENAME", fileSelectedName)
                            myItemView.context.startActivity(imageViewIntent)

                            //it.context.startActivity(Intent(it.context, ImageActivity::class.java))

                            //HomeActivity().startActivity(myIntent)
                        }

                        "Open in Web View" -> {

                        }

                        "Open with System" -> {

                        }

                        "Download in the Default App Folder" -> {

                        }

                        "Download in Specified Location" -> {

                        }

                        "Delete File" -> {
                            val deleteFileConfirmationDialog = MaterialAlertDialogBuilder(myItemView.context)
                            deleteFileConfirmationDialog
                                .setCancelable(false)
                                .setTitle("Delete File")
                                .setMessage("Do you want to permanently delete the file \"$fileSelectedName\" from the cloud?")
                                .setNegativeButton("CANCEL") { _, _ -> // DialogInterface!, Int
                                }
                                .setPositiveButton("DELETE") { _, _ ->
                                    myStorageRef.child(fileStorageRoot).child(fileSelectedName).delete().addOnSuccessListener {
                                        copyDataFromStorageToRealtimeDB(myItemView.context)
                                        showAlertDialog("File Deleted", "The file \"$fileSelectedName\" has been succesfully deleted from the cloud.", myItemView.context)
                                    }
                                    .addOnFailureListener { error ->
                                        showAlertDialog("File Deletion Error", error.localizedMessage!!, myItemView.context)
                                    }
                                }
                                .show()
                        }

                    }
                }
                .show()
        }
    }

}
