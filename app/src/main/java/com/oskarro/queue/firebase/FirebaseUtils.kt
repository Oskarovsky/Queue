package com.oskarro.queue.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.oskarro.queue.activities.SignUpActivity
import com.oskarro.queue.model.User

class FirebaseUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {



    }
}