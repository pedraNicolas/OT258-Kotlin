package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.businesslogic.GetTestimonialsUseCase
import com.melvin.ongandroid.businesslogic.getSlidesUseCase
import com.melvin.ongandroid.model.slides.SlidesDataModel
import com.melvin.ongandroid.model.testimonials.DataModel
import com.melvin.ongandroid.model.testimonials.TestimonialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val getTestimonialsUseCase: GetTestimonialsUseCase, private val getSlidesUseCase: getSlidesUseCase) :
    ViewModel() {
    val testimonialModel = MutableLiveData<List<DataModel>>()
    val slidesModel = MutableLiveData<List<SlidesDataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val slidesCallFailed = MutableLiveData<Boolean>()

    //This function refresh the testimonials livedata value with the use case response.
    //The "isLoading" value is waiting the query completion, can be use in a future for the implementation of the loading spinner
    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getTestimonialsUseCase()
            if (result.isNotEmpty()) {
                testimonialModel.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    // Recover the slides list to be used with the MotableLiveData of Slides list
    fun onCreateSlides(){
        viewModelScope.launch {
            var result = getSlidesUseCase()
            if (result.isNotEmpty()){
                slidesModel.postValue(result)
                slidesCallFailed.postValue(false)
            } else {
                slidesCallFailed.postValue(true)
            }
        }
    }
}