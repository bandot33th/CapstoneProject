package com.example.capstoneproject.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.repository.auth.UserRepository
import com.example.capstoneproject.data.response.auth.register.RegisterResponse

class RegisterViewModel(private val repository: UserRepository):ViewModel() {

    companion object{
        const val TAG="RegisterViewModel"
    }

    private lateinit var registerResponse: LiveData<RegisterResponse>

    fun register(name: String,username: String, password: String){
        registerResponse = repository.register(name,username,password)
    }
    fun getRegisterResponseLiveData(): LiveData<RegisterResponse> {
        return registerResponse
    }
}