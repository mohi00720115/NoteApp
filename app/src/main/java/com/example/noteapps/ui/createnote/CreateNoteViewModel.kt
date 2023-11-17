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

    fun check() {
        viewModelScope.launch {
            if (noteTitle.value.isEmpty()) {
                Log.e(TAG, "noteTitle: ${noteTitle.value}")
            } else if (noteSubTitle.value.isEmpty()) {
                Log.e(TAG, "noteSubTitle: ${noteSubTitle.value}")
            } else if (noteDesc.value.isEmpty()) {
                Log.e(TAG, "noteDesc: ${noteDesc.value}")
            }
        }
    }

    fun specificNote(noteId: Int) {
        viewModelScope.launch {
            repository.getSpecificNote(noteId).collect {
                _specificNote.value = it
            }
        }
    }

    fun insertNotes(note: Notes) {
        viewModelScope.launch {
            repository.insertNotes(note).collect()
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