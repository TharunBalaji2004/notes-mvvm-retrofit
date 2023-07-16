package com.tharunbalaji.mvvm_notes_app.api

import com.tharunbalaji.mvvm_notes_app.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()

        request.addHeader("Authorization","Bearer $token")

        return chain.proceed(request.build())
    }
}