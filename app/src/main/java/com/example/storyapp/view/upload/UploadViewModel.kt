package com.example.storyapp.view.upload

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): FileUploadResponse {
        return storyRepository.addStory(file, description, lat, lon)
    }
}
