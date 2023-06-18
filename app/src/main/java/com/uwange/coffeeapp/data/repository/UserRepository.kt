package com.uwange.coffeeapp.data.repository

import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun saveUserData(currentUser: FirebaseUser)
    fun getUserName(): String
}