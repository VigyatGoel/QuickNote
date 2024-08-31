package com.vigyat.quicknote.viewmodel

import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModelFactory(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val noteViewModel: NoteViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(auth, credentialManager, noteViewModel = noteViewModel) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}