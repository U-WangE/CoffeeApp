package com.uwange.coffeeapp.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.uwange.coffeeapp.data.repositoryImpl.ImageRepositoryImpl

interface ImageRepository {
    fun getImageUrls(): Task<List<Task<Uri>>>
}