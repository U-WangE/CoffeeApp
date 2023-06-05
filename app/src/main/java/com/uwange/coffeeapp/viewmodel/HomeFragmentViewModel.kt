package com.uwange.coffeeapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _imageUrls = MutableLiveData<List<Task<Uri>>>()
    val imageUrls: LiveData<List<Task<Uri>>> = _imageUrls

    fun fetchImageUrls() {
        imageRepository.getImageUrls()
            .addOnSuccessListener { urls ->
                _imageUrls.value = urls
            }
            .addOnFailureListener { exception ->
                // Handle failure to fetch image URLs
                //TODO::STOP REFRESH ICON
            }
    }
}