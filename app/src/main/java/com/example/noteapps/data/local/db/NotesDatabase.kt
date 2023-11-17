package com.example.noteapps.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapps.data.local.dao.NoteDao
import com.example.noteapps.data.local.entity.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase() : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}