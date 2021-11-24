package com.oskarro.queue.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.User
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirebaseUtils().loadUserData(this)
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
        Glide
            .with(this@MyProfileActivity)
            .load(user.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(my_profile_user_image)

        my_profile_name_et.setText(user.name)
        my_profile_email_et.setText(user.email)

        if (user.mobile != 0L) {
            my_profile_mobile_et.setText(user.mobile.toString())
        }
    }
}