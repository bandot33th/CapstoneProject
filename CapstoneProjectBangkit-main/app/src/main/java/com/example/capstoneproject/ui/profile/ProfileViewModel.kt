package com.example.capstoneproject.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.main.MainRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: MainRepository): ViewModel() {

    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}