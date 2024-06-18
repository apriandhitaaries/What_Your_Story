package com.example.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    fun getStoriesWithLocation(location: Int) {
        viewModelScope.launch {
            val response = storyRepository.getStoriesWithLocation(location)
            _stories.postValue(response)
        }
    }
}