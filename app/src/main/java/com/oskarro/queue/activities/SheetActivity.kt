package com.oskarro.queue.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.ProductDto
import com.oskarro.queue.model.Sheet
import com.oskarro.queue.model.Stage
import com.oskarro.queue.model.User
import com.oskarro.queue.utils.Constants
import com.oskarro.queue.utils.SheetValues
import kotlinx.android.synthetic.main.activity_google.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sheet.*
import org.json.JSONObject

class SheetActivity : BaseActivity() {

    lateinit var editGoogleSheetUrl: EditText
    lateinit var editGoogleSheetName: EditText

    lateinit var btnUpdateSheet: Button

    private lateinit var mSheetDetails: Sheet
    private var requestQueue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)

        setupActionBar()
        FirebaseUtils().loadCurrentSheetUrl(this)

        btnUpdateSheet = findViewById(R.id.btn_change_sheet)
        editGoogleSheetUrl = findViewById(R.id.edit_google_sheet_url)
        editGoogleSheetName = findViewById(R.id.edit_google_sheet_name)

        requestQueue = Volley.newRequestQueue(this@SheetActivity)

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
        setSupportActionBar(toolbar_sheet_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.sheet_title)
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

    // TODO
    fun checkSheet(url: String, tabName: String, sheetHashMap: HashMap<String, Any>) {
        val stringRequest = object: StringRequest(
            Method.GET,
            url.plus("?sheetTabName=${tabName}&sheetUrl=${url}"),
            Response.Listener { response ->
                val jsonObj = JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1))
                val productJson = jsonObj.getJSONArray(SheetValues.PRODUCTS)
                FirebaseUtils().updateSheetUrl(this, sheetHashMap)
                hideProgressDialog()
            },
            Response.ErrorListener {
                hideProgressDialog()
                Toast.makeText(this@SheetActivity, "Error has occurred!", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["requestMethod"] = SheetValues.MULTI_REQUEST
                return params
            }

        }
        requestQueue?.add(stringRequest)
    }

    fun sheetUrlUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}