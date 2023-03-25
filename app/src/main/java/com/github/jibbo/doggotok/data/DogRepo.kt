package com.github.jibbo.doggotok.data

import com.github.jibbo.doggotok.api.DogsApiService
import javax.inject.Inject

class DogRepo @Inject constructor(private val apiService: DogsApiService) {
    suspend fun getRandomDog() = apiService.getRandomDog()
}