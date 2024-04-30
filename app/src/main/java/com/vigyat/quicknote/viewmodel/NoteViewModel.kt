package com.vigyat.quicknote.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigyat.quicknote.model.repository.Repository
import com.vigyat.quicknote.model.room.Note
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) : ViewModel(), Observable {

    val notes = repository.getAllNotes()

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }


    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun getNoteById(id: Int): LiveData<Note> {
        return repository.getNoteById(id)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}
