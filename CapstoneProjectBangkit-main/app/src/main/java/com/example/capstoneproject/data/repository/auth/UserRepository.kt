package com.example.capstoneproject.data.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.data.model.UserPreference
import com.example.capstoneproject.data.model.auth.login.LoginModel
import com.example.capstoneproject.data.model.auth.register.RegisterModel
import com.example.capstoneproject.data.remote.login.ApiService
import com.example.capstoneproject.data.response.auth.login.LoginResponse
import com.example.capstoneproject.data.response.auth.register.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userPreference: UserPreference, private val apiService: ApiService) {



    private val registerResponse = MutableLiveData<RegisterResponse>()
    private val loginResponse = MutableLiveData<LoginResponse>()


    suspend fun saveSession(username: String) {
        userPreference.saveSession(username)
    }
    fun login(username: String, password: String): LiveData<LoginResponse>{
        val loginModel: LoginModel = LoginModel(username,password)
        apiService.login(loginModel).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    loginResponse.postValue(response.body())
                }
                else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
        return loginResponse
    }

    fun register(name: String, username: String, password: String):LiveData<RegisterResponse>{
        val registerModel: RegisterModel = RegisterModel(name,username,password)
        apiService.register(registerModel).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful){
                    registerResponse.postValue(response.body())
                }
                else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return registerResponse
    }
    companion object {
        const val TAG="UserRepository"
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}