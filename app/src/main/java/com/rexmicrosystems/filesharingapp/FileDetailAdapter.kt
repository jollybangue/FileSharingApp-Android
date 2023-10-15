package com.rexmicrosystems.filesharingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Make the alert dialogs cancellable or non-cancellable. "setCancelable" requires a boolean value. By default all alert dialogs are cancelable on button click or touch outside. If this method is set to false, you need to explicitly cancel the dialog using dialog.cancel() method.

class FileDetailAdapter(private var fileDetailList: List<FileDetail>): RecyclerView.Adapter<FileDetailAdapter.FileDetailViewHolder>() {

    class FileDetailViewHolder(fileDetailView: View): RecyclerView.ViewHolder(fileDetailView) {
        val textViewFileName: TextView = fileDetailView.findViewById(R.id.textViewFileName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDetailViewHolder {
        val myAdapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return FileDetailViewHolder(myAdapterLayout)
    }

    override fun getItemCount(): Int {
        return fileDetailList.size
    }

    override fun onBindViewHolder(holder: FileDetailViewHolder, position: Int) {

        var fileSelectedName = fileDetailList[position].name

        holder.textViewFileName.text = fileSelectedName

        // List of actions shown when the user click on a file
        val fileAction = arrayOf("Open in Image View", "Open in Web View", "Open with System", "Download in the Default App Folder", "Download in Specified Location")

        holder.itemView.setOnClickListener {

            val fileActionsDialog = MaterialAlertDialogBuilder(it.context)
            fileActionsDialog
                .setCancelable(false)
                .setTitle(fileSelectedName)

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

            //notifyItemChanged(position)

            //    In "file_item.xml", android:foreground="?selectableItemBackground" allows to highlight the selected item.
            //    Do I need  android:clickable="true" and android:focusable="true" ??? It seems NO!!!.
        }
    }

}