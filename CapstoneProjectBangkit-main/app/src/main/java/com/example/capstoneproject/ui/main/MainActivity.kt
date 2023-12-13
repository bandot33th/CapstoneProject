package com.example.capstoneproject.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.ui.HomeActivity
import com.example.capstoneproject.ui.ViewModelFactory
import com.example.capstoneproject.ui.adapter.HistoryAdapter
import com.example.capstoneproject.ui.profile.ProfileActivity
import com.example.capstoneproject.ui.scanner.ScannerActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var username: String? = null
    private val adapter = HistoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.trashRv.layoutManager = LinearLayoutManager(this)
        binding.trashRv.setHasFixedSize(true)
        mainViewModel.getSession().observe(this){
            username = it
            if(username == ""){
                startActivity(Intent(this,HomeActivity::class.java))
                finish()
            }
            else {
                username?.let {
                    mainViewModel.history(it)
                    mainViewModel.getListImagesItem().observe(this) {
                        adapter.setData(it)
                        binding.trashRv.adapter = adapter
                    }
                }
            }
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
                    startActivity(Intent(this,ScannerActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}