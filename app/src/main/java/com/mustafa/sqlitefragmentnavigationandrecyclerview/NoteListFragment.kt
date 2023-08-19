package com.mustafa.sqlitefragmentnavigationandrecyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mustafa.sqlitefragmentnavigationandrecyclerview.databinding.FragmentNoteListBinding
import java.lang.Exception

class NoteListFragment : Fragment() {

    private lateinit var binding : FragmentNoteListBinding
    private lateinit var notesAdaptor: NotesAdaptor
    private lateinit var dataBase : NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the database connection here
        try {
            dataBase = NoteDatabaseHelper(requireContext())
        } catch (e : Exception) {
            // Display a toast message in case of an exception
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set a listener for the toolbar's menu items
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addNote -> {
                    // Navigate to the AddNoteFragment when the "Add Note" menu item is clicked
                    val action = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
            true
        }

        // Initialize and set up the NotesAdaptor for the RecyclerView
        notesAdaptor = NotesAdaptor(ArrayList()) // Initialize the NotesAdaptor
        val recyclerView = binding.homeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = notesAdaptor

        try {
            // Retrieve all notes from the database and update the adaptor
            val notes = dataBase.getAllNotes()
            notesAdaptor.updateData(notes)
        } catch (e : Exception) {
            // Display a toast message in case of an exception
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
