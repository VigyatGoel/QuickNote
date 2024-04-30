package com.vigyat.quicknote.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        var isInitialDataLoaded = false


        addNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)

        addNoteBinding.saveBtn.isEnabled = false // disable the button initially

        val dao = NotesDatabase.getInstance(applicationContext).noteDao
        val repository = Repository(dao)
        val factory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Enable the button when text changes
                if (isInitialDataLoaded) {
                    addNoteBinding.saveBtn.isEnabled = true


                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        }

        addNoteBinding.titleEt.addTextChangedListener(textWatcher)
        addNoteBinding.contentEt.addTextChangedListener(textWatcher)

        noteId = intent.getIntExtra("noteId", -1)
        if (noteId != -1) {
            noteViewModel.getNoteById(noteId!!).observe(this) { note ->
                addNoteBinding.titleEt.setText(note.title)
                addNoteBinding.contentEt.setText(note.content)

                isInitialDataLoaded = true
            }

            addNoteBinding.AddORUpdateTV.text = "Update Note"
            addNoteBinding.saveBtn.text = "Update Note"
        } else {
            addNoteBinding.AddORUpdateTV.text = "Add a New Note"
            addNoteBinding.saveBtn.text = "Save Note"

            isInitialDataLoaded = true

        }

        addNoteBinding.saveBtn.setOnClickListener {

            val title = addNoteBinding.titleEt.text.toString()
            val content = addNoteBinding.contentEt.text.toString()
            val timeStamp = System.currentTimeMillis()

            if (noteId != -1) {
                noteViewModel.update(Note(noteId!!, title, content, timeStamp))
            } else {
                noteViewModel.insert(Note(0, title, content, timeStamp))
            }

            finish()
        }

    }


}