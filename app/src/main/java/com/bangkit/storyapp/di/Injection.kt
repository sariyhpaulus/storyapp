package com.bangkit.storyapp.di

import android.content.Context
import com.bangkit.storyapp.data.StoryRepository
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first()}
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref, apiService)
    }
}