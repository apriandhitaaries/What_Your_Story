package com.example.storyapp.data

import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        if (response.error == false) {
            val user = response.loginResult?.token?.let { UserModel(email, it, true) }
            if (user != null) {
                saveSession(user)
            }
        }
        return response
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ) = UserRepository(userPreference, apiService)
    }
}