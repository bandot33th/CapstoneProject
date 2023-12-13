package com.example.capstoneproject.di

import android.content.Context
import com.example.capstoneproject.data.model.UserPreference
import com.example.capstoneproject.data.model.dataStore
import com.example.capstoneproject.data.remote.login.ApiConfig
import com.example.capstoneproject.data.repository.auth.UserRepository
import com.example.capstoneproject.data.repository.main.MainRepository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref,apiService)
    }

    fun provideMainRepository(context: Context): MainRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = com.example.capstoneproject.data.remote.main.ApiConfig.getApiService()
        return MainRepository.getInstance(pref,apiService)
    }
}