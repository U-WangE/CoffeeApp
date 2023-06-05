package com.uwange.coffeeapp.data.repositoryImpl

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.uwange.coffeeapp.data.repository.ImageRepository

class ImageRepositoryImpl: ImageRepository {
    private val storageReference = Firebase.storage.reference
    private val imagesFolderRef = storageReference.child("advertising_images")

    override fun getImageUrls(): Task<List<Task<Uri>>> {
        return imagesFolderRef.listAll().continueWith { result ->
            result.result?.items?.map { storageReference ->
                storageReference.downloadUrl
            } ?: emptyList()
        }
    }
}