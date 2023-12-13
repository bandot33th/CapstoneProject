package com.example.capstoneproject.data.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstoneproject.data.model.UserPreference
import com.example.capstoneproject.data.remote.main.ApiService
import com.example.capstoneproject.data.response.main.history.HistoryResponse

import com.example.capstoneproject.data.response.main.history.ImagesItem
import com.example.capstoneproject.data.response.main.predict.PredictResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

class MainRepository(private val userPreference: UserPreference, private val apiService: ApiService) {


    private val _listImagesItem = MutableLiveData<ArrayList<ImagesItem>>()
    private val _predictResultResponse = MutableLiveData<String>()
    fun getSession(): Flow<String> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }
    fun predict(multipartBody: MultipartBody.Part, username: String): LiveData<String>{
        val username = username.toRequestBody("text/plain".toMediaType())
        val client = apiService.predict(multipartBody,username)
        client.enqueue(object :Callback<PredictResponse>{
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val data = response.body()
                if(response.isSuccessful){
                    if (data!=null){
                        _predictResultResponse.value = data.result
                    }
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return _predictResultResponse
    }
    fun history(username: String): LiveData<ArrayList<ImagesItem>>{

        val username = username.toRequestBody("text/plain".toMediaType())
        val client = apiService.history(username)

        client.enqueue(object :Callback<HistoryResponse>{
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                val data = response.body()
                if (response.isSuccessful){
                    if (data != null) {
                        _listImagesItem.value = ArrayList(data.images)
                    }
                }
            }
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
        return _listImagesItem
    }
    companion object {
        const val TAG="UserRepository"
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(userPreference,apiService)
            }.also { instance = it }
    }
}