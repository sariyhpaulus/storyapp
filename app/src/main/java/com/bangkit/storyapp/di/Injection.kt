package com.bangkit.storyapp.di

import android.content.Context
import com.bangkit.storyapp.data.database.StoryRepository
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.database.StoryDatabase
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository.getInstance(database, pref, apiService)
    }
}