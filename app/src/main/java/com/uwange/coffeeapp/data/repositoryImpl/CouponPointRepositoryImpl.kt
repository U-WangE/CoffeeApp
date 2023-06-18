package com.uwange.coffeeapp.data.repositoryImpl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.uwange.coffeeapp.data.repository.CouponPointRepository
import com.uwange.coffeeapp.sharedpreference.CoffeePreference
import com.uwange.coffeeapp.utils.CommonUtil.errorLog
import javax.inject.Inject

class CouponPointRepositoryImpl @Inject constructor(
    private val pref: CoffeePreference
): CouponPointRepository {
    private val databaseReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("couponPoint")
    }

    // firebase database에서 coupon 값 가져오기
    override fun insertCouponPointData(currentUser: FirebaseUser) {
        databaseReference.child(currentUser.uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val couponPoint = snapshot.getValue(Int::class.java)

                    if (couponPoint != null)
                        pref.couponPoint = couponPoint
                    else
                        updateCouponPointData(currentUser, 0)
                }

                override fun onCancelled(error: DatabaseError) {
                    errorLog(error.message, null, error.details)
                }
            })
    }

    override fun getCouponPointData(): Int {
        return pref.couponPoint
    }

    // firebase database에 update
    override fun updateCouponPointData(currentUser: FirebaseUser, couponPoint: Int) {
        databaseReference.child(currentUser.uid)
            .setValue(couponPoint)
            .addOnSuccessListener {
                pref.couponPoint = couponPoint
            }
            .addOnFailureListener { exception ->
                errorLog(exception.message, exception.cause, null)
            }
    }


}