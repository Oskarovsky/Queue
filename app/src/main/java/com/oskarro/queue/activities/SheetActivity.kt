package com.oskarro.queue.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.Sheet
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_google.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sheet.*

class SheetActivity : BaseActivity() {

    lateinit var editGoogleSheetUrl: EditText
    lateinit var editGoogleSheetName: EditText
    lateinit var btnUpdateSheet: Button

    private lateinit var mSheetDetails: Sheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)

        setupActionBar()
        FirebaseUtils().loadCurrentSheetUrl(this)

        btnUpdateSheet = findViewById(R.id.btn_change_sheet)
        editGoogleSheetUrl = findViewById(R.id.edit_google_sheet_url)
        editGoogleSheetName = findViewById(R.id.edit_google_sheet_name)

        btnUpdateSheet.setOnClickListener {
            if (editGoogleSheetUrl.text.toString().trim().isBlank()) {
                Toast.makeText(this@SheetActivity, "URL field cannot be empty! ", Toast.LENGTH_SHORT).show()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                updateSheetUrl()
            }
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
        toolbar_sheet_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun setSheetUrlInUI(sheet: Sheet) {
        mSheetDetails = sheet

        edit_google_sheet_url.setText(mSheetDetails.url)
        edit_google_sheet_name.setText(mSheetDetails.tabName)
    }

    private fun updateSheetUrl() {
        val sheetHashMap = HashMap<String, Any>()
        var anyChangesMade = false

        if (edit_google_sheet_url.text.toString() != mSheetDetails.url || edit_google_sheet_name.text.toString() != mSheetDetails.tabName) {
            sheetHashMap[Constants.SHEET_URL] = edit_google_sheet_url.text.toString()
            sheetHashMap[Constants.SHEET_NAME] = edit_google_sheet_name.text.toString()
            sheetHashMap[Constants.SHEET_TAB_NAME] = edit_google_sheet_name.text.toString()
            sheetHashMap[Constants.SHEET_ACTIVE] = true
            anyChangesMade = true
        }

        if (anyChangesMade) {
            FirebaseUtils().updateSheetUrl(this, sheetHashMap)
        } else {
            Toast.makeText(this,"Nothing as been updated!",Toast.LENGTH_SHORT).show()
            hideProgressDialog()
        }
    }

    fun sheetUrlUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}