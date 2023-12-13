package com.example.capstoneproject.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.data.repository.auth.UserRepository
import com.example.capstoneproject.data.repository.main.MainRepository
import com.example.capstoneproject.di.Injection
import com.example.capstoneproject.ui.auth.AuthViewModelFactory
import com.example.capstoneproject.ui.auth.login.LoginViewModel
import com.example.capstoneproject.ui.auth.register.RegisterViewModel
import com.example.capstoneproject.ui.main.MainViewModel
import com.example.capstoneproject.ui.profile.ProfileViewModel
import com.example.capstoneproject.ui.scanner.ScannerViewModel

class ViewModelFactory private constructor(private val repository: MainRepository): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ScannerViewModel::class.java) -> {
                ScannerViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideMainRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}