package com.github.jibbo.doggotok.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jibbo.doggotok.data.DogRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val repo: DogRepo) : ViewModel() {
    companion object {
        const val PAGINATION = 5
    }

    val pictureFlow = mutableStateListOf<Result<String>>()

    init {
        for (i in 0..PAGINATION) {
            randomDog()
        }
    }

    fun randomDog() {
        viewModelScope.launch {
            try {
                pictureFlow.add(Result.success(repo.getRandomDog().url))
            } catch (e: Exception) {
                pictureFlow.add(Result.failure(e))
            }
        }
    }

}