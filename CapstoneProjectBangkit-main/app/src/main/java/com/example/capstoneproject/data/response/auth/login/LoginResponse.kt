package com.example.capstoneproject.data.response.auth.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("token")
	val token: String? = null
)
