package com.mustafa.sqlitefragmentnavigationandrecyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.mustafa.sqlitefragmentnavigationandrecyclerview.databinding.FragmentAddNoteBinding
import java.lang.Exception

class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var noteList: ArrayList<Note>
    private lateinit var dataBase: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nothing specific happening in onCreate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
        // Return the inflated view for the fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize required variables and components
        noteList = ArrayList()
        try {
            dataBase = NoteDatabaseHelper(requireContext())
            noteList = dataBase.getAllNotes()

        } catch (e: Exception) {
            // Handle exceptions that might occur while initializing the database
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
        }

        // Set up the fragment's UI and functionality
        getPosition()
        approveNote(view)
        deleteNotes(view)
        changeNote(view)

    }

    private fun getPosition() {
        arguments?.let {
            // Get the position argument from the navigation bundle
            val position = AddNoteFragmentArgs.fromBundle(it).Position
            if (position != -1) {
                // If a valid position is provided, populate UI with note data
                binding.titleText.setText(noteList[position].title)
                binding.titleText.isClickable = false
                binding.explainText.setText(noteList[position].content)
                binding.deleteButton.visibility = View.VISIBLE
                binding.changeButton.visibility = View.VISIBLE
                binding.approveButton.visibility = View.GONE
            }
        }
    }

    private fun approveNote(view: View) {
        binding.approveButton.setOnClickListener {
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
            val name = binding.titleText.text.toString()
            val note = binding.explainText.text.toString().toInt()
            if (note in 0..100) {
                try {
                    // Add a new note to the database
                    dataBase.addNote(name, note.toString())
                } catch (e: Exception) {
                    // Handle exceptions that might occur while adding a note
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                }
                // Navigate to the NoteListFragment
                Navigation.findNavController(it).navigate(action)
            } else {
                // Display a toast if the note value is not within the allowed range
                Toast.makeText(
                    requireContext(),
                    "Enter a Number From Zero to Hundred",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changeNote(view: View) {
        binding.changeButton.setOnClickListener {
            arguments?.let {
                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
                val position = AddNoteFragmentArgs.fromBundle(it).Position
                val note = binding.explainText.text.toString().toInt()
                if (note in 0..100) {
                    try {
                        // Update an existing note in the database
                        dataBase.updateNote(
                            noteList[position].id,
                            noteList[position].title,
                            note.toString()
                        )
                    } catch (e: Exception) {
                        // Handle exceptions that might occur while updating a note
                        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                    // Navigate to the NoteListFragment
                    Navigation.findNavController(view).navigate(action)
                } else {
                    // Display a toast if the note value is not within the allowed range
                    Toast.makeText(
                        requireContext(),
                        "Enter a Number From Zero to Hundred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun deleteNotes(view: View) {
        binding.deleteButton.setOnClickListener {
            arguments?.let {
                val position = AddNoteFragmentArgs.fromBundle(it).Position
                try {
                    // Delete an existing note from the database
                    dataBase.deleteNote(noteList[position].id)
                } catch (e: Exception) {
                    // Handle exceptions that might occur while deleting a note
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                }
                // Navigate to the NoteListFragment
                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
}