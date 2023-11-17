package com.example.noteapps.data.repository

import com.example.noteapps.data.local.dao.NoteDao
import com.example.noteapps.data.local.entity.Notes
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(private val noteDao: NoteDao) {

    suspend fun getAllNotes() = flow {
        emit(noteDao.getAllNotes())
    }

    suspend fun getSpecificNote(noteId: Int) = flow {
        emit(noteDao.getSpecificNote(noteId))
    }

    suspend fun insertNotes(note: Notes) = flow {
        emit(noteDao.insertNotes(note))
    }

    suspend fun deleteNote(note: Notes) = flow {
        emit(noteDao.deleteNote(note))
    }

    suspend fun deleteSpecificNote(id: Int) = flow {
        emit(noteDao.deleteSpecificNote(id))
    }

    suspend fun updateNote(note: Notes) = flow {
        emit(noteDao.updateNote(note))
    }

}
