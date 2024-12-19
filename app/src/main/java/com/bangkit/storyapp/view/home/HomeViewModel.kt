package com.bangkit.storyapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.storyapp.data.StoryRepository
import com.bangkit.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
     private val storyRepository: StoryRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<PagingData<ListStoryItem>>()
    val listStory: LiveData<PagingData<ListStoryItem>> = _listStory

    fun getStoriesWithToken() {
        viewModelScope.launch {
            storyRepository.getSession().collect {
                fetchListStory()
            }
        }
    }

    private fun fetchListStory() {
        viewModelScope.launch {
            storyRepository.getStories()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _listStory.postValue(pagingData)
                }
        }
    }
}