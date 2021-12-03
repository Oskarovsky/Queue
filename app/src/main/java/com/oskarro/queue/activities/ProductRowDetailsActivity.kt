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
import com.oskarro.queue.utils.SheetValues


class ProductRowDetailsActivity : BaseActivity() {

    lateinit var btnUpdateStageInSheet: Button

    private var mProductOrderNumber: String = ""

    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_row_details)

        requestQueue = Volley.newRequestQueue(this@ProductRowDetailsActivity)

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

        btnUpdateStageInSheet.setOnClickListener {
            val url = Constants.GOOGLE_SCRIPT
            showProgressDialog(resources.getString(R.string.please_wait))
            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    val intent = Intent(this@ProductRowDetailsActivity, ProductRowDetailsActivity::class.java)
                    hideProgressDialog()
                    startActivity(intent)
                    Toast.makeText(this@ProductRowDetailsActivity, "Product stage has been updated", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@ProductRowDetailsActivity, "Error has occurred!", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productStatus"] = spinnerProductStage.selectedItem.toString()
                    params["productCode"] = mProductOrderNumber
                    return params
                }
            }
            requestQueue?.add(stringRequest)
        }

        getIntentData()
        showProgressDialog(resources.getString(R.string.please_wait))
        fetchProductRowFromSheet(mProductOrderNumber)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_row_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
        }
        toolbar_product_row_details_activity.setNavigationOnClickListener{
            startActivity(Intent(this@ProductRowDetailsActivity, GoogleReadActivity::class.java))
        }
    }

    private fun fetchProductRowFromSheet(orderNumber: String) {
        val uri = Constants.GOOGLE_SCRIPT + "?requestMethod=SINGLE&productCode=$orderNumber"
        val stringRequest = object: StringRequest(
            Method.GET,
            uri,
            Response.Listener { response ->
                val productJson = JSONObject(response)
                val dto = ProductDto()
                dto.stage = productJson.getString(SheetValues.STAGE)
                dto.orderNumber = productJson.getString(SheetValues.CODE)
                dto.name = productJson.getString(SheetValues.NAME)
                dto.invoiceNumber = productJson.getString(SheetValues.INVOICE_NUMBER)
                dto.client = productJson.getString(SheetValues.CLIENT)
                dto.productType = productJson.getString(SheetValues.TYPE)
                dto.quantity = productJson.getString(SheetValues.QUANTITY)
                dto.price = productJson.getString(SheetValues.PRICE)
                populateProductToUI(dto)
                hideProgressDialog()
            },
            Response.ErrorListener {
                Toast.makeText(this@ProductRowDetailsActivity, "Error has occurred!", Toast.LENGTH_SHORT).show()
            }
        ) {
        }
        requestQueue?.add(stringRequest)
    }

    fun populateProductToUI(productDto: ProductDto) {
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