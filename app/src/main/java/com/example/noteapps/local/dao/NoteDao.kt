package com.example.noteapps.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.noteapps.local.entity.Notes

@Dao
interface NoteDao {
    @Query("SELECT * FROM Notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM Notes WHERE id =:id")
    suspend fun getSpecificNote(id: Int): Notes

    @Insert(onConflict = REPLACE)
    suspend fun insertNotes(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Query("DELETE FROM Notes WHERE id =:id")
    suspend fun deleteSpecificNote(id: Int)

    @Update
    suspend fun updateNote(note: Notes)
}