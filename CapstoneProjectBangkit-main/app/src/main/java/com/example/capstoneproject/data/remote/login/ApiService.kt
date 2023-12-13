package com.example.capstoneproject.data.remote.login

import com.example.capstoneproject.data.model.auth.login.LoginModel
import com.example.capstoneproject.data.model.auth.register.RegisterModel
import com.example.capstoneproject.data.response.auth.login.LoginResponse
import com.example.capstoneproject.data.response.auth.register.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    fun register(@Body registerModel: RegisterModel?): Call<RegisterResponse>

    @POST("login")
    fun login(@Body loginModel: LoginModel?): Call<LoginResponse>
}