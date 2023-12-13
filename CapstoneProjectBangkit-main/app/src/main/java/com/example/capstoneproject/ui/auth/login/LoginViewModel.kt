package com.example.capstoneproject.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.auth.UserRepository
import com.example.capstoneproject.data.response.auth.login.LoginResponse
import com.example.capstoneproject.data.response.auth.register.RegisterResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository):ViewModel() {

    companion object{
        const val TAG="LoginViewModel"
    }
    fun saveSession(username: String) {
        viewModelScope.launch {
            repository.saveSession(username)
        }
    }
    private lateinit var loginResponse: LiveData<LoginResponse>
    fun login(username: String, password: String){
        loginResponse = repository.login(username,password)
    }

    fun getLoginResponseLiveData(): LiveData<LoginResponse> {
        return loginResponse
    }
}