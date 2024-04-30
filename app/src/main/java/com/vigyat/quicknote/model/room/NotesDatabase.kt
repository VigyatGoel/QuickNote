package com.vigyat.quicknote.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)

abstract class NotesDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao


    //Singleton database design

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {

            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {

                    // creating the new database object
                    instance = Room.databaseBuilder(
                        context.applicationContext, NotesDatabase::class.java, "notes_db"
                    ).build()
                }
                INSTANCE = instance
                return instance
            }
        }


    }


}


