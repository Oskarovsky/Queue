package com.oskarro.queue.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_update.*

class GoogleUpdateActivity : BaseActivity() {

    lateinit var editProductStatus: EditText
    lateinit var btnUpdateInSheet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_update)

        setupActionBar()

        btnUpdateInSheet = findViewById(R.id.btn_update_in_sheet)

        val spinnerStatus: Spinner = findViewById(R.id.spinner_update_status)
        val paths = arrayOf("NEW", "IN-PROGRESS", "DONE")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paths)
        spinnerStatus.adapter = arrayAdapter

        spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        btnUpdateInSheet.setOnClickListener {
            val url = Constants.GOOGLE_SCRIPT
            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    Toast.makeText(this@GoogleUpdateActivity, "TEST", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@GoogleUpdateActivity, "TEST", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productCode"] = "A000608" // TODO
                    params["productStatus"] = spinnerStatus.selectedItem.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this@GoogleUpdateActivity)
            queue.add(stringRequest)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_update_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_product_update_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}