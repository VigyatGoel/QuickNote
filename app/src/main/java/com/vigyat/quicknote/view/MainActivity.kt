package com.vigyat.quicknote.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vigyat.quicknote.R
import com.vigyat.quicknote.adapter.NotesAdapter
import com.vigyat.quicknote.databinding.ActivityMainBinding
import com.vigyat.quicknote.model.repository.Repository
import com.vigyat.quicknote.model.room.Note
import com.vigyat.quicknote.model.room.NotesDatabase
import com.vigyat.quicknote.viewmodel.NoteViewModel
import com.vigyat.quicknote.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {


    private lateinit var addBtn: FloatingActionButton
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        addBtn = mainBinding.addBtn

        val dao = NotesDatabase.getInstance(applicationContext).noteDao
        val repository = Repository(dao)
        val factory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        initRecyclerView()

        addBtn.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        displayNotesList()
    }

    private fun displayNotesList() {

        noteViewModel.notes.observe(this, Observer {
            mainBinding.recyclerView.adapter = NotesAdapter(this, it)
        })

    }

    fun deleteNote(note: Note) {
        noteViewModel.delete(note)
    }

}
