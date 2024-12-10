package com.bangkit.storyapp.view.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.storyapp.data.StoryRepository
import com.bangkit.storyapp.data.api.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
     private val storyRepository: StoryRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: MutableLiveData<List<ListStoryItem>> = _listStory

    fun getStoriesWithToken() {
        viewModelScope.launch {
            storyRepository.getSession().collect {
                fetchListStory()
            }
        }
    }

    private fun fetchListStory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = storyRepository.getStories()
                _listStory.value = response.listStory?.filterNotNull() ?: emptyList()
                Log.d("HomeViewModel", "fetchListStory: $response")
            } catch (e: Exception) {
                _listStory.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}