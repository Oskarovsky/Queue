package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_write.*

class GoogleWriteActivity : BaseActivity() {

    lateinit var editProductStatus: EditText
    lateinit var editProductName: EditText
    lateinit var btnSaveToGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_write)

        setupActionBar()

        editProductStatus = findViewById(R.id.edit_product_status)
        editProductName = findViewById(R.id.edit_product_name)
        btnSaveToGoogle = findViewById(R.id.btn_save_to_google)


        val spinnerStatus: Spinner = findViewById(R.id.spinner_status)
        val paths = arrayOf("NEW", "IN-PROGRESS", "DONE")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paths)
        spinnerStatus.adapter = arrayAdapter

        spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        btnSaveToGoogle.setOnClickListener {
            val url = "https://script.google.com/macros/s/AKfycbwRQ0bYKpPifDY_bMyCXl16iJczH1AMeRuGpJBvNd_hxKfctI7Axwor7oCKaCOeyExt/exec"
            val stringRequest = object: StringRequest(Method.POST, url,
                Response.Listener {
                    Toast.makeText(this@GoogleWriteActivity, "TEST", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@GoogleWriteActivity, "TEST", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productCode"] = editProductName.text.toString()
                    params["productStatus"] = spinnerStatus.selectedItem.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this@GoogleWriteActivity)
            queue.add(stringRequest)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_write_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_product_write_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}