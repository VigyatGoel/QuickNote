package com.vigyat.quicknote.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.vigyat.quicknote.R
import com.vigyat.quicknote.databinding.ActivityLoginBinding
import com.vigyat.quicknote.viewmodel.LoginViewModel
import com.vigyat.quicknote.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, start MainActivity
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // This will finish LoginActivity
            return
        }
        val factory = LoginViewModelFactory(auth)

        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val credentialManager = CredentialManager.create(this)

        binding.googleSignInBtn.setOnClickListener {

            loginViewModel.loginWithGoogle(this, credentialManager, getString(R.string.web_client_id))

        }

        loginViewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // This will finish LoginActivity
            } else {
                Toast.makeText(this@LoginActivity, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        })


    }
}