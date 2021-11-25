package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.Board
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class CreateBoardActivity : BaseActivity() {

    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        setupActionBar()

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        btn_create_board_create.setOnClickListener {
            createBoard()
        }
    }

    private fun createBoard() {
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserID())
        val board = Board(et_create_board_name.text.toString(), mUserName, assignedUsersArrayList)
        FirebaseUtils().createBoard(this, board)
    }

    fun boardCreatedSuccessfully() {
        hideProgressDialog()
        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.create_board_title)
        }
        toolbar_create_board_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}