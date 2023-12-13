package com.example.capstoneproject.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityProfileBinding
import com.example.capstoneproject.ui.HomeActivity
import com.example.capstoneproject.ui.ViewModelFactory
import com.example.capstoneproject.ui.main.MainActivity
import com.example.capstoneproject.ui.main.MainViewModel
import com.example.capstoneproject.ui.scanner.ScannerActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener{
            profileViewModel.logout()
            startActivity(Intent(this,HomeActivity::class.java))
        }

        profileViewModel.getSession().observe(this){
            binding.textView2.text = it
        }
        binding.navView.setOnNavigationItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home-> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_scan->{
                    startActivity(Intent(this, ScannerActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}