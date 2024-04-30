package com.vigyat.quicknote.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("select * from notes order by timeStamp desc")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("select * from notes where id = :id")
    fun getNoteById(id: Int): LiveData<Note>

    @Query("SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query")
    fun getFilteredNotes(query: String): LiveData<List<Note>>


}