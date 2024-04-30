package com.vigyat.quicknote.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vigyat.quicknote.R
import com.vigyat.quicknote.databinding.ItemNoteBinding
import com.vigyat.quicknote.model.room.Note
import com.vigyat.quicknote.view.AddNoteActivity
import com.vigyat.quicknote.view.MainActivity

class NotesAdapter(private val context: Context, private var notes: List<Note>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {


    inner class NotesViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.titleTV.text = note.title

            binding.contentTV.text = note.content

            binding.cardView.setOnClickListener {
                val intent = Intent(context, AddNoteActivity::class.java).apply {
                    putExtra("noteId", note.id)
                }
                context.startActivity(intent)
            }

            binding.optionsButton.setOnClickListener { view ->
                // Create a PopupMenu, inflate it with `context_menu.xml` and show it
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.note_options_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {

                        R.id.menu_delete -> {
                            // Handle delete action
                            showDeleteConfirmationDialog(note)
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNoteBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_note, parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {

        holder.bind(notes[position])
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(context)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                // Delete the note
                (context as MainActivity).deleteNote(note)
                Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("No", null)
            .show()
    }
}

