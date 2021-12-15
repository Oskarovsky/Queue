package com.oskarro.queue.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.oskarro.queue.activities.*
import com.oskarro.queue.model.Sheet
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants

class FirebaseUtils {
    private val fireStoreDatabase = FirebaseFirestore.getInstance()

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
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }


    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        fireStoreDatabase.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully!")
                Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board")
                Toast.makeText(activity, "Error when updating profile", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {
        fireStoreDatabase.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    when (activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                        }
                        is MyProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener {
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("SignInUser", "Error has occurred during registration")
            }
    }



    fun updateSheetUrl(activity: SheetActivity, sheetHashMap: HashMap<String, Any>) {
        fireStoreDatabase.collection(Constants.SHEETS)
            .document("1")
            .update(sheetHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Sheet URL updated successfully!")
                Toast.makeText(activity, "Sheet URL updated successfully", Toast.LENGTH_SHORT).show()
                activity.sheetUrlUpdateSuccess()
            }.addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating Sheet URL")
                Toast.makeText(activity, "Error when updating Sheet URL", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadCurrentSheetUrl(activity: Activity) {
        fireStoreDatabase.collection(Constants.SHEETS)
            .document("1")
            .get()
            .addOnSuccessListener { document ->
                val currentSheet = document.toObject(Sheet::class.java)
                if (currentSheet != null) {
                    when (activity) {
                        is SheetActivity -> {
                            Log.d("SHEET", currentSheet.toString())
                            activity.setSheetUrlInUI(currentSheet)
                        }
                        is GoogleReadActivity -> {
                            Log.d("SHEET 2", currentSheet.toString())
                            activity.setSheetUrlForRequest(currentSheet)
                        }
                    }
                }
            }.addOnFailureListener {
                when (activity) {
                    is SheetActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("SheetActivity", "Error has occurred during registration")
            }
    }

}