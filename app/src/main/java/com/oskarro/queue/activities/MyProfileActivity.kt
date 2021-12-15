package com.oskarro.queue.activities

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {

    private var mProfileImageURL : String = ""
    private lateinit var mUserDetails: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirebaseUtils().loadUserData(this)

        my_profile_update_btn.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            updateUserProfileData()
        }
    }


    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }
        toolbar_my_profile_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    fun setUserDataInUI(user: User) {
        mUserDetails = user

        Glide
            .with(this@MyProfileActivity)
            .load(user.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        my_profile_name_et.setText(user.name)
        my_profile_email_et.setText(user.email)

        if (user.mobile != 0L) {
            my_profile_mobile_et.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade : Boolean = false

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.imageUrl) {
            userHashMap[Constants.IMAGE_URL] = mProfileImageURL
            anyChangesMade = true
        }

        if (my_profile_name_et.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = my_profile_name_et.text.toString()
            anyChangesMade = true
        }

        if (my_profile_mobile_et.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = my_profile_mobile_et.text.toString().toLong()
            anyChangesMade = true
        }

        if (anyChangesMade) {
            FirebaseUtils().updateUserProfileData(this, userHashMap)
        } else {
            Toast.makeText(this,"Nothing as been updated!",Toast.LENGTH_SHORT).show()
            hideProgressDialog()
        }
    }


    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}