package com.oskarro.queue.activities

import android.os.Bundle
import android.util.Log
import com.oskarro.queue.R
import com.oskarro.queue.model.Board
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private var mProcessListPosition: Int = -1
    private var mProductPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        getIntentData()
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = mBoardDetails.processList[mProcessListPosition].products[mProductPosition].name
        }
        toolbar_product_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.PROCESS_LIST_ITEM_POSITION)) {
            mProcessListPosition = intent.getIntExtra(Constants.PROCESS_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.PRODUCT_LIST_ITEM_POSITION)) {
            mProductPosition = intent.getIntExtra(Constants.PRODUCT_LIST_ITEM_POSITION, -1)
        }
    }
}