package com.vigyat.quicknote.viewmodel

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.vigyat.quicknote.model.room.Note
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SettingsViewModel(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,

    noteViewModel: NoteViewModel
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