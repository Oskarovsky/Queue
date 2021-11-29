package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oskarro.queue.R
import com.oskarro.queue.model.Board
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_row_details.*

class ProductRowDetailsActivity : AppCompatActivity() {

    private lateinit var mBoardDetails: Board
    private var mProductRowPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_row_details)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_row_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_product_row_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}