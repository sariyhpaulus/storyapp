package com.bangkit.storyapp.di

import android.content.Context
import com.bangkit.storyapp.data.StoryRepository
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository.getInstance(pref, apiService)
    }
}