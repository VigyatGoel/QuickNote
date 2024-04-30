package com.vigyat.quicknote.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.vigyat.quicknote.R
import com.vigyat.quicknote.databinding.ActivityAddNoteBinding
import com.vigyat.quicknote.model.repository.Repository
import com.vigyat.quicknote.model.room.Note
import com.vigyat.quicknote.model.room.NotesDatabase
import com.vigyat.quicknote.viewmodel.NoteViewModel
import com.vigyat.quicknote.viewmodel.NoteViewModelFactory

class AddNoteActivity : AppCompatActivity() {


    private lateinit var addNoteBinding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var noteId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)

        val dao = NotesDatabase.getInstance(applicationContext).noteDao
        val repository = Repository(dao)
        val factory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        noteId = intent.getIntExtra("noteId", -1)
        if (noteId != -1) {
            noteViewModel.getNoteById(noteId!!).observe(this) { note ->
                addNoteBinding.titleEt.setText(note.title)
                addNoteBinding.contentEt.setText(note.content)
            }

            addNoteBinding.AddORUpdateTV.text = "Update Note"
            addNoteBinding.saveBtn.text = "Update Note"
        } else {
            addNoteBinding.AddORUpdateTV.text = "Add a New Note"
            addNoteBinding.saveBtn.text = "Save Note"

        }

        addNoteBinding.saveBtn.setOnClickListener {

            val title = addNoteBinding.titleEt.text.toString()
            val content = addNoteBinding.contentEt.text.toString()

            if (noteId != -1) {
                noteViewModel.update(Note(noteId!!, title, content))
            } else {
                noteViewModel.insert(Note(0, title, content))
            }

            finish()
        }

    }


}