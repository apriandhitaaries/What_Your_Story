package com.example.storyapp.view.signup

import ErrorResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val resultLiveData = MutableLiveData<RegisterResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(name, email, password)
                resultLiveData.postValue(result)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                resultLiveData.postValue(RegisterResponse(error = true, message = errorMessage))
            } finally {
                _isLoading.value = false
            }
        }
        return resultLiveData
    }
}