package com.example.storyapp.view.detail

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.DetailStoryResponse

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return storyRepository.getDetailStory(id)
    }
}