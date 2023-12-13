package com.example.capstoneproject.data.response.main.predict

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("result")
	val result: String
)
