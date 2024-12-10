package com.bangkit.storyapp.utils

import com.bangkit.storyapp.data.api.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

fun parseErrorMessage(e: HttpException): String {
    return try {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse.message ?: "Terjadi kesalahan"
    } catch (exception: Exception) {
        "Terjadi kesalahan"
    }
}

interface LogoutCallback {
    fun onLogout()
}
