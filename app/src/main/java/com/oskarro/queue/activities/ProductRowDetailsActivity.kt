package com.oskarro.queue.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

class ProductRowDetailsActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var mBoardDocumentId: String

    private var mProductOrderNumber: String = ""

    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_row_details)

        setupActionBar()

        getIntentData()
        showProgressDialog(resources.getString(R.string.please_wait))
        Log.d("TEST", "TEST PARAM -- $mProductOrderNumber")
        requestQueue = Volley.newRequestQueue(this@ProductRowDetailsActivity)
        fetchProductRowFromSheet(mProductOrderNumber)

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
        val url = Constants.GOOGLE_SCRIPT
        val stringRequest = object: StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                Log.d("TEST", "TEST PARAM -- $orderNumber")
                Log.d("TEST", "TEST -- $response")
                val productJson = JSONObject(response)
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
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["requestMethod"] = "SINGLE"
                params.put("requestMethod", "SINGLE")
                params["requestCode"] = orderNumber.toString()
                return params
            }

        }
        requestQueue?.add(stringRequest)
        Log.d("TEST", "TEST REQUEST -- $stringRequest")
    }

    fun populateProductToUI(productDto: ProductDto) {
        rv_row_products_list.visibility = View.VISIBLE
        tv_no_row_products.visibility = View.GONE

        rv_row_products_list.layoutManager = LinearLayoutManager(this)
        rv_row_products_list.setHasFixedSize(true)

        val adapter = ProductDtoAdapter(this, productDto)
        rv_row_products_list.adapter = adapter

    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.PRODUCT_ORDER_NUMBER)) {
            mProductOrderNumber = intent.getStringExtra(Constants.PRODUCT_ORDER_NUMBER)!!
        }
    }
}