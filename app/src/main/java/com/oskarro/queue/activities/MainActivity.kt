package com.oskarro.queue.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_REQUEST_CODE : Int = 11
        const val CREATE_BOARD_REQUEST_CODE : Int = 12
        const val READ_DATA_REQUEST_CODE : Int = 13
        const val GOOGLE_SHEET_REQUEST_CODE : Int = 14
    }

    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        nav_view.setNavigationItemSelectedListener(this)

        FirebaseUtils().loadUserData(this, true)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Pokaż dostępne opcje"
        }
    }

    private fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {
            FirebaseUtils().loadUserData(this)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.nav_read_data -> {
                startActivityForResult(Intent(this, GoogleActivity::class.java), READ_DATA_REQUEST_CODE)
            }
            R.id.nav_set_excel -> {
                startActivityForResult(Intent(this, SheetActivity::class.java), GOOGLE_SHEET_REQUEST_CODE)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        mUserName = user.name

        Glide
            .with(this)
            .load(user.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(nav_user_image)

        tv_username.text = user.name
    }


}