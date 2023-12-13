package com.example.capstoneproject.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.data.repository.main.MainRepository
import com.example.capstoneproject.data.response.main.history.ImagesItem
import okhttp3.MultipartBody

class ScannerViewModel(private val repository: MainRepository): ViewModel() {

    companion object{
        const val TAG ="ScannerViewModel"
    }


    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }

    private lateinit var predictResultResponse : LiveData<String>
    fun predict(multipartBody: MultipartBody.Part,username: String){
        predictResultResponse = repository.predict(multipartBody,username)
    }

    fun getPredictResultResponse(): LiveData<String> {
        return predictResultResponse
    }
}