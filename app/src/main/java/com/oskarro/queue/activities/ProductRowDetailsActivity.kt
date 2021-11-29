package com.oskarro.queue.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.adapters.ProductDtoAdapter
import com.oskarro.queue.adapters.ProductRowsAdapter
import com.oskarro.queue.model.Board
import com.oskarro.queue.model.ProductDto
import com.oskarro.queue.utils.Constants
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_row_details.*
import org.json.JSONObject
import android.R.string.no
import android.widget.*


class ProductRowDetailsActivity : BaseActivity() {

    lateinit var btnUpdateStageInSheet: Button

    private var mProductOrderNumber: String = ""

    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_row_details)

        setupActionBar()

        btnUpdateStageInSheet = findViewById(R.id.btn_update_product_row_status)

        val spinnerProductStage: Spinner = findViewById(R.id.spinner_product_status)
        val paths = arrayOf("NEW", "IN-PROGRESS", "DONE")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paths)
        spinnerProductStage.adapter = arrayAdapter

        spinnerProductStage.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        getIntentData()
        showProgressDialog(resources.getString(R.string.please_wait))
        Log.d("TEST", "TEST PARAM -- $mProductOrderNumber")
        requestQueue = Volley.newRequestQueue(this@ProductRowDetailsActivity)
        fetchProductRowFromSheet(mProductOrderNumber)


        btnUpdateStageInSheet.setOnClickListener {
            val url = Constants.GOOGLE_SCRIPT
            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    Toast.makeText(this@ProductRowDetailsActivity, "TEST", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@ProductRowDetailsActivity, "TEST", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productStatus"] = spinnerProductStage.selectedItem.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this@ProductRowDetailsActivity)
            queue.add(stringRequest)
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_row_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_product_row_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun fetchProductRowFromSheet(orderNumber: String) {
        val uri = Constants.GOOGLE_SCRIPT + "?requestMethod=SINGLE&productCode=$orderNumber"
        val stringRequest = object: StringRequest(
            Method.GET,
            uri,
            Response.Listener { response ->
                Log.d("TEST", "TEST PARAM -- $orderNumber")
                Log.d("TEST", "TEST -- $response")
                val productJson = JSONObject(response)
                Log.d("TEST", "TEST JSON-- $productJson")
                val dto = ProductDto()
                dto.stage = productJson.getString("productStatus")
                dto.orderNumber = productJson.getString("productCode")
                dto.name = productJson.getString("productName")
                hideProgressDialog()
                populateProductToUI(dto)
            },
            Response.ErrorListener {
                Toast.makeText(this@ProductRowDetailsActivity, "ERROR TEST", Toast.LENGTH_SHORT).show()
            }
        ) {
        }
        requestQueue?.add(stringRequest)
        Log.d("TEST", "TEST REQUEST -- $stringRequest")
    }

    fun populateProductToUI(productDto: ProductDto) {
//        val adapter = ProductDtoAdapter(this, productDto)
        tv_row_product_dto_name.text = productDto.name
        tv_row_product_dto_code.text = productDto.orderNumber
        tv_row_product_dto_stage.text = productDto.stage

    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.PRODUCT_ORDER_NUMBER)) {
            mProductOrderNumber = intent.getStringExtra(Constants.PRODUCT_ORDER_NUMBER)!!
        }
    }
}