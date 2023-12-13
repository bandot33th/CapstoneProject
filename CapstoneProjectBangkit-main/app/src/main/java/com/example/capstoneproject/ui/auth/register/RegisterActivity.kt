package com.example.capstoneproject.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.ui.auth.AuthViewModelFactory
import com.example.capstoneproject.ui.auth.login.LoginActivity
import com.example.capstoneproject.ui.auth.login.LoginViewModel

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.registerButton.setOnClickListener{
            val name = binding.edRegisterName.text.toString()
            val username = binding.edRegisterUsername.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            registerViewModel.register(name,username,password)
            registerViewModel.getRegisterResponseLiveData().observe(this){registerResponse->
                if (registerResponse.error == false){
                    startActivity(Intent(this,LoginActivity::class.java))
                }
            }
        }

    }
}