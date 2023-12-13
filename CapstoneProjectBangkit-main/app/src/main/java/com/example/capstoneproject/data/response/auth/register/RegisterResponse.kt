package com.example.capstoneproject.data.response.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("Error")
	val error: Boolean? = null
)
