package com.rexmicrosystems.filesharingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileDetailAdapter(var fileDetailList: List<FileDetail>): RecyclerView.Adapter<FileDetailAdapter.FileDetailViewHolder>() {

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
        holder.textViewFileName.text = fileDetailList[position].name
    }

}