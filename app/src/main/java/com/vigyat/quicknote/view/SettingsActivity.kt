package com.vigyat.quicknote.view

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.credentials.CredentialManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vigyat.quicknote.R
import com.vigyat.quicknote.databinding.ActivitySettingsBinding
import com.vigyat.quicknote.model.repository.Repository
import com.vigyat.quicknote.model.room.NotesDatabase
import com.vigyat.quicknote.viewmodel.NoteViewModel
import com.vigyat.quicknote.viewmodel.NoteViewModelFactory
import com.vigyat.quicknote.viewmodel.SettingsViewModel
import com.vigyat.quicknote.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutBtn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val scope = CoroutineScope(Dispatchers.Main)
        val currentUser = auth.currentUser
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        logoutBtn = binding.logoutBtn

        val dao = NotesDatabase.getInstance(applicationContext).noteDao
        val repository = Repository(dao)
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        val credentialManager = CredentialManager.create(this)


        val settingsFactory = SettingsViewModelFactory(auth, credentialManager, noteViewModel)

        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
        settingsViewModel = ViewModelProvider(this, settingsFactory)[SettingsViewModel::class.java]




        binding.backupCard.setOnClickListener {

        }

        binding.restoreCard.setOnClickListener {
        }

        val button = logoutBtn
        ViewCompat.setOnApplyWindowInsetsListener(button) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

        logoutBtn.setOnClickListener {
            settingsViewModel.logout()
        }
        settingsViewModel.logoutStatus.observe(this, Observer { isLoggedOut ->
            if (isLoggedOut) {
                val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // This will finish SettingsActivity
            } else {
                Toast.makeText(this@SettingsActivity, "Logout failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


}