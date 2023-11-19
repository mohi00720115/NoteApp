package com.example.noteapps.ui.createnote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapps.data.local.entity.Notes
import com.example.noteapps.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _specificNote = MutableStateFlow<Notes>(Notes())
    val specificNote: StateFlow<Notes> = _specificNote

    val noteTitle = MutableStateFlow<String>("")
    val noteSubTitle = MutableStateFlow<String>("")
    val noteDesc = MutableStateFlow<String>("")

    fun check(): Boolean {
        return if (noteTitle.value.isEmpty() || noteSubTitle.value.isEmpty() || noteDesc.value.isEmpty()) {
            Log.e(TAG, "noteTitle.value: ${noteTitle.value}")
            Log.e(TAG, "noteSubTitle.value: ${noteSubTitle.value}")
            Log.e(TAG, "noteDesc.value: ${noteDesc.value}")
            false
        }else true

    }

    fun insertNotes(note: Notes) {
        viewModelScope.launch {
            noteTitle.value = note.title.toString()
            noteSubTitle.value = note.subTitle.toString()
            noteDesc.value = note.noteText.toString()
            repository.insertNotes(note).collect()
        }
    }

    fun specificNote(noteId: Int) {
        viewModelScope.launch {
            repository.getSpecificNote(noteId).collect {
                _specificNote.value = it
            }
        }
    }


    fun deleteSpecificNote(id: Int) {
        viewModelScope.launch {
            repository.deleteSpecificNote(id).collect()
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch {
            repository.updateNote(note).collect()
        }
    }


}