package com.vigyat.quicknote.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vigyat.quicknote.R
import com.vigyat.quicknote.databinding.ActivityLoginBinding
import com.vigyat.quicknote.viewmodel.LoginViewModel
import com.vigyat.quicknote.viewmodel.LoginViewModelFactory


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

            loginViewModel.loginWithGoogle(
                this,
                credentialManager,
                getString(R.string.web_client_id)
            )

        }

        loginViewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            try {
                if (isLoggedIn) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // This will finish LoginActivity
                } else {
                    Toast.makeText(this@LoginActivity, "Sign in failed", Toast.LENGTH_SHORT).show()
                    Log.d("login", "login error")
                }
            } catch (e: Exception) {
                Log.d("login-check", "login error $e")
            }
        })


    }
}