package com.uwange.coffeeapp.data.repositoryImpl

import com.google.firebase.auth.FirebaseUser
import com.uwange.coffeeapp.data.repository.UserRepository
import com.uwange.coffeeapp.sharedpreference.CoffeePreference
import javax.inject.Inject

data class UserData(
    var userId: String? = null,
    var userName: String? = null,
)

class UserRepositoryImpl @Inject constructor(
    private val pref: CoffeePreference
): UserRepository {
    private var userData: UserData? = null

    override fun saveUserData(currentUser: FirebaseUser) {
        userData =
            UserData(
                userId = currentUser.uid,
                userName =
                if (!currentUser.displayName.isNullOrBlank()) currentUser.displayName else "Unknown"
            )

        pref.userData = userData
    }
}