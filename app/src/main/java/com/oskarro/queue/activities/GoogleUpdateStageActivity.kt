package com.oskarro.queue.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.Sheet
import com.oskarro.queue.model.Stage
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_update_stage.*

class GoogleUpdateStageActivity : BaseActivity() {

    lateinit var editProductInvoiceNumber: EditText
    lateinit var editProductOrderNumber: EditText
    lateinit var btnSaveToGoogle: Button

    private lateinit var sheetObject: Sheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_update_stage)

        setupActionBar()
        FirebaseUtils().loadCurrentSheetUrl(this@GoogleUpdateStageActivity)


        editProductInvoiceNumber = findViewById(R.id.edit_product_invoice_number)
        editProductOrderNumber = findViewById(R.id.edit_product_order_number)
        btnSaveToGoogle = findViewById(R.id.btn_save_to_google)


        val spinnerStatus: Spinner = findViewById(R.id.spinner_status)
        val availableStages = Stage.values().map { it.value }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, availableStages)
        spinnerStatus.adapter = arrayAdapter

        spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        btnSaveToGoogle.setOnClickListener {
            if (editProductOrderNumber.text.toString().trim().isBlank() || editProductInvoiceNumber.text.toString().trim().isBlank()) {
                Toast.makeText(this@GoogleUpdateStageActivity, "Both fields cannot be empty! ", Toast.LENGTH_SHORT).show()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                val url = Constants.GOOGLE_SCRIPT
                val stringRequest = object: StringRequest(
                    Method.POST,
                    url.plus("?sheetTabName=${sheetObject.tabName}&sheetUrl=${sheetObject.url}"),
                    Response.Listener {
                        finish();
                        startActivity(intent);
                        hideProgressDialog()
                        Toast.makeText(this@GoogleUpdateStageActivity, "Product stage updated", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@GoogleUpdateStageActivity, "Could not update product stage", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["productOrderNumber"] = editProductOrderNumber.text.toString()
                        params["productInvoiceNumber"] = editProductInvoiceNumber.text.toString()
                        params["productStatus"] = spinnerStatus.selectedItem.toString()
                        return params
                    }
                }
                val queue = Volley.newRequestQueue(this@GoogleUpdateStageActivity)
                queue.add(stringRequest)
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_write_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.product_stage_board_title)
        }
        toolbar_product_write_activity.setNavigationOnClickListener{
            startActivity(Intent(this@GoogleUpdateStageActivity, GoogleActivity::class.java))
        }
    }

    fun setSheetUrlForRequest(sheet: Sheet) {
        sheetObject = sheet
    }
}