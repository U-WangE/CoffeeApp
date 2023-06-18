package com.uwange.coffeeapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

interface CouponPointRepository {
    fun updateCouponPointData(currentUser: FirebaseUser, couponPoint: Int)
    fun insertCouponPointData(currentUser: FirebaseUser)
    fun getCouponPointData(): Int
}