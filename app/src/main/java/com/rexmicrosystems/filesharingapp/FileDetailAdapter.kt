package com.rexmicrosystems.filesharingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        // Alert Dialog buttons: Neutral: "FILE DETAILS", Positive: "SHARE", Negative: "CANCEL"
        val fileAction = arrayOf("Open in Image View", "Open in Web View", "Open with System", "Download in the Default App Folder", "Download in Specified Location")

        var fileSelectedName = fileDetailList[position].name

        holder.textViewFileName.text = fileSelectedName

        holder.itemView.setOnClickListener {

            MaterialAlertDialogBuilder(it.context)
                .setPositiveButton("SHARE") {_, _ ->
                    Toast.makeText(it.context, "SHARE selected", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("CANCEL") {_, _ ->
                    Toast.makeText(it.context, "CANCEL selected", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("FILE DETAILS") {_, _ ->
                    Toast.makeText(it.context, "FILE DETAILS selected", Toast.LENGTH_SHORT).show()
                }
                .setItems(fileAction) { dialog, i ->
                    Toast.makeText(it.context, "You selected \"${fileAction[i]}\" action", Toast.LENGTH_SHORT).show()

                    println("Content of dialog local variable: $dialog")
                    println("Content of i local variable: $i")
                }
                .setTitle(fileSelectedName)
                .show()

            //notifyItemChanged(position)

            //    In "file_item.xml", android:foreground="?selectableItemBackground" allows to highlight the selected item.
            //    Do I need  android:clickable="true" and android:focusable="true" ??? It seems NO!!!.
        }
    }

}