package com.oskarro.queue.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarro.queue.R
import com.oskarro.queue.adapters.ProcessListItemsAdapter
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.Board
import com.oskarro.queue.model.Process
import com.oskarro.queue.model.Product
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_process_list.*

class ProcessListActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var mBoardDocumentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_list)

        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().getBoardDetails(this, mBoardDocumentId)
    }

    override fun onResume() {
        FirebaseUtils().getBoardDetails(this, mBoardDocumentId)
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MEMBERS_REQUEST_CODE) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseUtils().getBoardDetails(this@ProcessListActivity, mBoardDocumentId)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    fun productDetails(processListPosition: Int, productPosition: Int) {
        val intent = Intent(this@ProcessListActivity, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
        intent.putExtra(Constants.PROCESS_LIST_ITEM_POSITION, processListPosition)
        intent.putExtra(Constants.PRODUCT_LIST_ITEM_POSITION, productPosition)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this@ProcessListActivity, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_process_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = mBoardDetails.name
        }
        toolbar_process_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun boardDetails(board: Board) {
        mBoardDetails = board

        hideProgressDialog()
        setupActionBar()

        val addProcessList = Process("Add list")
        mBoardDetails.processList.add(addProcessList)

        rv_process_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_process_list.setHasFixedSize(true)

        val adapter = ProcessListItemsAdapter(this@ProcessListActivity, mBoardDetails.processList)
        rv_process_list.adapter = adapter
    }

    fun addUpdateProcessListSuccess() {
        hideProgressDialog()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().getBoardDetails(this@ProcessListActivity, mBoardDetails.documentId)
    }

    fun createProcessList(processListName: String) {
        val process = Process(processListName, FirebaseUtils().getCurrentUserId())
        mBoardDetails.processList.add(0, process)
        mBoardDetails.processList.removeAt(mBoardDetails.processList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().addUpdateProcessList(this, mBoardDetails)
    }

    fun updateProcessList(position: Int, listName: String, model: Process) {
        val process = Process(listName, model.createdBy)
        mBoardDetails.processList[position] = process
        mBoardDetails.processList.removeAt(mBoardDetails.processList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().addUpdateProcessList(this@ProcessListActivity, mBoardDetails)
    }

    fun deleteProcessList(position: Int) {
        mBoardDetails.processList.removeAt(position)
        mBoardDetails.processList.removeAt(mBoardDetails.processList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().addUpdateProcessList(this@ProcessListActivity, mBoardDetails)
    }

    fun addProductToProcessList(position: Int, productName: String) {
        mBoardDetails.processList.removeAt(mBoardDetails.processList.size - 1)
        val productAssignedUsersList: ArrayList<String> = ArrayList()
        productAssignedUsersList.add(FirebaseUtils().getCurrentUserId())
        val product = Product(productName, FirebaseUtils().getCurrentUserId(), productAssignedUsersList)
        val productList = mBoardDetails.processList[position].products
        productList.add(product)
        val process = Process(
            mBoardDetails.processList[position].title,
            mBoardDetails.processList[position].createdBy,
            productList
        )
        mBoardDetails.processList[position] = process
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseUtils().addUpdateProcessList(this@ProcessListActivity, mBoardDetails)
    }

    companion object {
        const val MEMBERS_REQUEST_CODE: Int = 13
        const val PRODUCT_DETAILS_REQUEST_CODE : Int = 14
    }
}