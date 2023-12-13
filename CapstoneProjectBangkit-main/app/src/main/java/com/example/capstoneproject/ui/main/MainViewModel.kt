package com.example.capstoneproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.auth.UserRepository
import com.example.capstoneproject.data.repository.main.MainRepository
import com.example.capstoneproject.data.response.auth.login.LoginResponse
import com.example.capstoneproject.data.response.main.history.ImagesItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository): ViewModel() {

    companion object{
        const val TAG ="MainViewModel"
    }


    fun getSession(): LiveData<String>{
        return repository.getSession().asLiveData()
    }

    private lateinit var listImagesItem : LiveData<ArrayList<ImagesItem>>
    fun history(username: String){
        listImagesItem = repository.history(username)
    }

    fun getListImagesItem(): LiveData<ArrayList<ImagesItem>> {
        return listImagesItem
    }

}