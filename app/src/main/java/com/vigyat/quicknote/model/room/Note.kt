package com.vigyat.quicknote.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var content: String,
    var timeStamp: Long
)
