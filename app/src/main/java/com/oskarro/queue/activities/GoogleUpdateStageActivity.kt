package com.oskarro.queue.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.model.Stage
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_update_stage.*

class GoogleUpdateStageActivity : BaseActivity() {

    lateinit var editProductStatus: EditText
    lateinit var editProductName: EditText
    lateinit var btnSaveToGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_update_stage)

        setupActionBar()

        editProductStatus = findViewById(R.id.edit_product_status)
        editProductName = findViewById(R.id.edit_product_name)
        btnSaveToGoogle = findViewById(R.id.btn_save_to_google)


        val spinnerStatus: Spinner = findViewById(R.id.spinner_status)
        val availableStages = Stage.values().map { it.toString() }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableStages)
        spinnerStatus.adapter = arrayAdapter

        spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        btnSaveToGoogle.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            val url = Constants.GOOGLE_SCRIPT
            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    val intent = Intent(this@GoogleUpdateStageActivity, GoogleReadActivity::class.java)
                    hideProgressDialog()
                    startActivity(intent)
                    Toast.makeText(this@GoogleUpdateStageActivity, "Product stage updated", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@GoogleUpdateStageActivity, "Could not update product stage", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productCode"] = editProductName.text.toString()
                    params["productStatus"] = spinnerStatus.selectedItem.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this@GoogleUpdateStageActivity)
            queue.add(stringRequest)
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
}