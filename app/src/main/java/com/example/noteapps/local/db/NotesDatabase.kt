package com.example.noteapps.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapps.local.dao.NoteDao
import com.example.noteapps.local.entity.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase() : RoomDatabase() {
    companion object {
        var noteDatabase: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NotesDatabase {
            if (noteDatabase != null) {
                noteDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "notes.db"
                ).build()
            }
            return noteDatabase!!
        }
    }
    abstract fun noteDao() : NoteDao
}