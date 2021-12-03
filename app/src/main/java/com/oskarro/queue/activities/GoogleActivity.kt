package com.oskarro.queue.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.oskarro.queue.R
import kotlinx.android.synthetic.main.activity_google.*
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_main.*

class GoogleActivity : AppCompatActivity() {

    lateinit var btnGoogleRead: Button
    lateinit var btnGoogleUpdateStage: Button
    lateinit var btnGoogleUpdateStageByCode: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        setupActionBar()

        btnGoogleRead = findViewById(R.id.btn_read_data_from_google)
        btnGoogleUpdateStage = findViewById(R.id.btn_update_product_stage_into_google)
        btnGoogleUpdateStageByCode = findViewById(R.id.btn_update_product_stage_by_barcode_into_google)

        btnGoogleRead.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleReadActivity::class.java)
            startActivity(intent)
        }

        btnGoogleUpdateStage.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleUpdateStageActivity::class.java)
            startActivity(intent)
        }

        btnGoogleUpdateStageByCode.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleUpdateStageByCodeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_google_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.main_board_title)
        }
        toolbar_google_activity.setNavigationOnClickListener { onBackPressed() }
    }
}
