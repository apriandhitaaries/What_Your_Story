package com.example.storyapp.di

import android.content.Context
import android.util.Log
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.StoryDatabase
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val apiService = ApiConfig.getApiService(token)

        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val apiService = ApiConfig.getApiService(token)
        val storyDatabase = StoryDatabase.getDatabase(context)

        return StoryRepository.getInstance(pref, apiService, storyDatabase)
    }
}