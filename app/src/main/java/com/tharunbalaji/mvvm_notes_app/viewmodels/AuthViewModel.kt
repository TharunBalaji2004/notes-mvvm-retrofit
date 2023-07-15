package com.tharunbalaji.mvvm_notes_app.viewmodels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tharunbalaji.mvvm_notes_app.models.UserRequest
import com.tharunbalaji.mvvm_notes_app.models.UserResponse
import com.tharunbalaji.mvvm_notes_app.repository.UserRepository
import com.tharunbalaji.mvvm_notes_app.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest = userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest = userRequest)
        }
    }

    fun validateCredentials(userName: String, emailAddress: String, password: String, isLogin: Boolean): Pair<Boolean,String> {
        var result = Pair(true, "")

        if ((!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Kindly provide credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Invalid Email Address")
        } else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }

        return result
    }

}