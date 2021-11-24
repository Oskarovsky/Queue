package com.oskarro.queue.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.oskarro.queue.activities.SignUpActivity
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants

class FirebaseUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        fireStoreDatabase.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                e -> Log.e(activity.javaClass.simpleName, "Error has occurred during registration")
            }


    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}