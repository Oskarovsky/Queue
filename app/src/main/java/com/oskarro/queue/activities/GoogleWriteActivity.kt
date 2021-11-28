package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R

class GoogleWriteActivity : BaseActivity() {

    lateinit var editProductStatus: EditText
    lateinit var editProductName: EditText
    lateinit var btnSaveToGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_write)

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
            val url: String = "https://script.google.com/macros/s/AKfycbx6GbiYm3055KO_QB3pbJekwrP2GGu1Itbeh2iQCd2VfQFnTY3qeHscVIq6v-D_ulp9/exec"
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
}