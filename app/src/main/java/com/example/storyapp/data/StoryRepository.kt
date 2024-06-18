package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.local.StoryDatabase
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): FileUploadResponse {
        return apiService.addStory(file, description, lat, lon)
    }

    suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        return apiService.getStoriesWithLocation(location)
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ) = StoryRepository (userPreference, apiService, storyDatabase)
    }
}
