package com.vigyat.quicknote.model.repository

import androidx.lifecycle.LiveData
import com.vigyat.quicknote.model.room.Note
import com.vigyat.quicknote.model.room.NoteDao

class Repository(private val noteDao: NoteDao) {

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    fun getNoteById(id: Int): LiveData<Note> {
        return noteDao.getNoteById(id)
    }

    suspend fun insert(note: Note): Long {
        return noteDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        return noteDao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        return noteDao.deleteNote(note)
    }


}