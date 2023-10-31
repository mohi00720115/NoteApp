package com.example.noteapps.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.noteapps.local.entity.Notes

interface NoteDao {
    @get:Query("SELECT * FROM Notes ORDER BY id DESC")
    val allNotes: List<Notes>

    @Insert(onConflict = REPLACE)
    fun insertNotes(note: Notes)

    @Delete
    fun deleteNote(note: Notes)
}