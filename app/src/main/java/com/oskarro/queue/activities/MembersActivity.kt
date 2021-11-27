package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarro.queue.R
import com.oskarro.queue.adapters.MemberListItemAdapter
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.Board
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class MembersActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!!
        }

        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    fun setupMembersList(list: ArrayList<User>) {
        hideProgressDialog()
        rv_members_list.layoutManager = LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)
        val adapter = MemberListItemAdapter(this, list)
        rv_members_list.adapter = adapter
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_members_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.members)
        }
        toolbar_members_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}