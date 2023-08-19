package com.mustafa.sqlitefragmentnavigationandrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mustafa.sqlitefragmentnavigationandrecyclerview.databinding.RecyclerviewRowBinding

class NotesAdaptor(var notesList : ArrayList<Note>) :
    RecyclerView.Adapter<NotesAdaptor.NotesHolder>() {

    // ViewHolder class that holds the views for each item
    class NotesHolder(var binding: RecyclerviewRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    // Called when the ViewHolder is created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return NotesHolder(view)
    }

    // Returns the number of items in the list
    override fun getItemCount(): Int = notesList.size

    // Binds data to the views in each item
    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        val list = notesList[position]

        // Bind title and content to respective TextViews
        holder.binding.recyclerViewNameText.text = list.title
        holder.binding.recyclerViewNoteText.text = list.content

        // Bind ID to ID TextView
        holder.binding.idRecyclerViewText.text = list.id.toString()

        // Set click listener for each item
        holder.itemView.setOnClickListener {
            // Navigate to the AddNoteFragment with the position argument
            val action = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(position)
            Navigation.findNavController(it).navigate(action)
        }
    }

    // Update the adapter with new data
    fun updateData(newNotes: ArrayList<Note>) {
        notesList.clear()
        notesList.addAll(newNotes)
        notifyDataSetChanged()
    }
}
