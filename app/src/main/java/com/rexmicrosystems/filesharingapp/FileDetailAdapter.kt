package com.rexmicrosystems.filesharingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        val fileAction = arrayOf("Open in Image View", "Open in Web View", "Open with System", "Download in the Default App Folder", "Download in Specified Location")

        holder.itemView.setOnClickListener {
            // VERY IMPORTANT NOTE: In "file_item.xml", android:foreground="?selectableItemBackground" allows to highlight the selected item.
            // Do I need  android:clickable="true" and android:focusable="true" ??? It seems NO!!!.

            //notifyItemChanged(position) // TODO: Check if it is necessary...

            // Creating fileActionsDialog which will be shown when the user clicks on a file. NOTE: Unable to define fileActionsDialog outside .setOnClickListener{}...
            val fileActionsDialog = MaterialAlertDialogBuilder(it.context)
            fileActionsDialog
                .setTitle(fileSelectedName)
                .setCancelable(false) // The user should select an item or a button to dismiss the alert dialog.

                .setPositiveButton("SHARE") {_, _ ->
                    // TODO: Implement SHARE file action
                    Toast.makeText(it.context, "SHARE selected", Toast.LENGTH_SHORT).show()
                }

                .setNegativeButton("CANCEL") {_, _ ->
                    // TODO: Implement CANCEL file action
                    Toast.makeText(it.context, "CANCEL selected", Toast.LENGTH_SHORT).show()
                }

                .setNeutralButton("FILE DETAILS") {_, _ ->
                    // TODO: Show file details
                    Toast.makeText(it.context, "FILE DETAILS selected", Toast.LENGTH_SHORT).show()
                }

                .setItems(fileAction) { dialog, i ->
                    Toast.makeText(it.context, "You selected \"${fileAction[i]}\" action", Toast.LENGTH_SHORT).show()
                    // TODO: Insert a Switch...Case control flow here. Each case corresponds to one file action (see content of array fileAction above.)
                    // Switch fileAction[i]
                    // Case "Open in Image View"
                    // Case "Open in Web View"...
                    println("Content of dialog local variable: $dialog")
                    println("Content of i local variable: $i")
                }
                .show()
        }
    }

}