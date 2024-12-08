package com.bangkit.storyapp.di

import android.content.Context
import com.bangkit.storyapp.data.UserRepository
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(userPreference, apiService)
    }
}