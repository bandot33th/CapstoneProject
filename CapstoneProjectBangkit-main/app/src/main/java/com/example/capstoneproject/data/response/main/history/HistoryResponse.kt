package com.example.capstoneproject.data.response.main.history

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("images")
	val images: List<ImagesItem>
)

data class ImagesItem(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("username")
	val username: String
)
