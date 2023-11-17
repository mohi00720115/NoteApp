package com.example.noteapps.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapps.data.local.entity.Notes
import com.example.noteapps.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _getAllNote = MutableStateFlow<List<Notes>>(emptyList())
    val getAllNote: StateFlow<List<Notes>> = _getAllNote

    /*    private val _getSpecificNote = MutableStateFlow<Int>(0)
        val getSpecificNote: StateFlow<Int> = _getSpecificNote*/

    fun getAllNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collect {
                _getAllNote.value = it
            }
        }
    }

    /*    fun getSpecificNote(noteId:Int) {
            viewModelScope.launch {
                repository.getSpecificNote(noteId).collect {
                    _getSpecificNote.value = it
                }
            }
        }*/

}