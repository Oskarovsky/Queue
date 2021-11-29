package com.oskarro.queue.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.oskarro.queue.R
import kotlinx.android.synthetic.main.activity_google.*
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_read.toolbar_product_read_activity

class GoogleActivity : AppCompatActivity() {

    lateinit var btnGoogleRead: Button
    lateinit var btnGoogleWrite: Button
    lateinit var btnGoogleUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        setupActionBar()

        btnGoogleRead = findViewById(R.id.btn_read_data_from_google)
        btnGoogleWrite = findViewById(R.id.btn_write_data_into_google)
        btnGoogleUpdate = findViewById(R.id.btn_update_data_into_google)

        btnGoogleRead.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleReadActivity::class.java)
            startActivity(intent)
        }

        btnGoogleWrite.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleWriteActivity::class.java)
            startActivity(intent)
        }

        btnGoogleUpdate.setOnClickListener {
            val intent = Intent(this@GoogleActivity, GoogleUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_google_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_google_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}
