package com.mustafa.sqlitefragmentnavigationandrecyclerview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NoteDatabase"
        private const val TABLE_NAME = "notes"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        // Create the notes table with columns: id (primary key), title, and content
        val createTable = ("CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_CONTENT TEXT)")
        db.execSQL(createTable)
    }

    // Called when the database needs to be upgraded (e.g., version change)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing notes table and recreate it
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Add a new note to the database
    fun addNote(title: String, content: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, title)
        values.put(KEY_CONTENT, content)
        return db.insert(TABLE_NAME, null, values)
    }

    // Retrieve all notes from the database
    @SuppressLint("Range")
    fun getAllNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            // Loop through the cursor to create Note objects and add them to the list
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                val content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT))
                notes.add(Note(id, title, content))
            } while (cursor.moveToNext())
            cursor.close()
        }
        return notes
    }

    // Update a note's information in the database
    fun updateNote(id: Int, title: String, content: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, title)
        values.put(KEY_CONTENT, content)
        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(id.toString()))
    }

    // Delete a note from the database
    fun deleteNote(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(id.toString()))
    }
}
