package com.example.capstoneproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.databinding.ActivityHomeBinding
import com.example.capstoneproject.ui.auth.login.LoginActivity
import com.example.capstoneproject.ui.auth.register.RegisterActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginActivityButton.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.registerActivityButton.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}