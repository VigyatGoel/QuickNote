package com.vigyat.quicknote.viewmodel

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,

    private val noteViewModel: NoteViewModel
) : ViewModel() {

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    fun logout() {
        viewModelScope.launch {
            try {
                auth.signOut()
                credentialManager.clearCredentialState(
                    ClearCredentialStateRequest()
                )
                _logoutStatus.value = true
            } catch (e: Exception) {
                _logoutStatus.value = false
            }
        }
    }


}