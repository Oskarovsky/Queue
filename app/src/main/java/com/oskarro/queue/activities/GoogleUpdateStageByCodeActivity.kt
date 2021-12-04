package com.oskarro.queue.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.model.ProductDto
import com.oskarro.queue.model.Stage
import com.oskarro.queue.utils.Constants
import com.oskarro.queue.utils.SheetValues
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_google_update_stage_by_code.*
import kotlinx.android.synthetic.main.activity_product_row_details.*
import org.json.JSONObject

class GoogleUpdateStageByCodeActivity : BaseActivity() {

    lateinit var btnUpdateInSheet: Button
    lateinit var btnScanCode: Button
    lateinit var tvProductCodeResult: TextView
    lateinit var tvProductCurrentStage: TextView
    lateinit var tvProductName: TextView
    lateinit var tvProductInvoice: TextView
    lateinit var tvProductImageUrl: TextView

    private lateinit var mOrderNumber: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_update_stage_by_code)

        setupActionBar()

        btnUpdateInSheet = findViewById(R.id.btn_update_in_sheet)
        btnScanCode = findViewById(R.id.btn_scan_code)
        tvProductCodeResult = findViewById(R.id.tv_product_code_result)
        tvProductCurrentStage = findViewById(R.id.tv_product_current_stage)
        tvProductName = findViewById(R.id.tv_product_current_name)
        tvProductInvoice = findViewById(R.id.tv_product_invoice)
        tvProductImageUrl = findViewById(R.id.tv_product_image)

        btnScanCode.setOnClickListener {
            startActivity(Intent(this@GoogleUpdateStageByCodeActivity, BarcodeScannerActivity::class.java))
        }

        if (intent.hasExtra("orderNumber")) {
            mOrderNumber = intent.getStringExtra("orderNumber").toString()
            tvProductCodeResult.text = mOrderNumber
            showProgressDialog(resources.getString(R.string.please_wait))
            fetchProductRowFromSheet(mOrderNumber)
        }

        val spinnerStatus: Spinner = findViewById(R.id.spinner_update_status)
        val availableStages = Stage.values().map { it.value }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, availableStages)
        spinnerStatus.adapter = arrayAdapter

        spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnUpdateInSheet.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            val url = Constants.GOOGLE_SCRIPT
            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener {
                    hideProgressDialog()
                    Toast.makeText(this@GoogleUpdateStageByCodeActivity, "Product stage updated", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@GoogleUpdateStageByCodeActivity, "Could not update product stage", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["productCode"] = mOrderNumber
                    params["productStatus"] = spinnerStatus.selectedItem.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this@GoogleUpdateStageByCodeActivity)
            queue.add(stringRequest)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_update_by_code_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.product_stage_board_title)
        }
        toolbar_product_update_by_code_activity.setNavigationOnClickListener {
            startActivity(Intent(this@GoogleUpdateStageByCodeActivity, GoogleActivity::class.java))
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
                dto.stage = Stage.fromString(productJson.getString(SheetValues.STAGE))
                dto.invoiceNumber = productJson.getString(SheetValues.INVOICE_NUMBER)
                dto.orderNumber = productJson.getString(SheetValues.CODE)
                dto.name = productJson.getString(SheetValues.NAME)
                dto.imageUlr = productJson.getString(SheetValues.IMAGE_URL)
                hideProgressDialog()
                populateProductToUI(dto)
            },
            Response.ErrorListener {
                Toast.makeText(this@GoogleUpdateStageByCodeActivity, "Error has occurred!", Toast.LENGTH_SHORT).show()
            }
        ) {
        }
        val queue = Volley.newRequestQueue(this@GoogleUpdateStageByCodeActivity)
        queue.add(stringRequest)
    }

    fun populateProductToUI(productDto: ProductDto) {
        tvProductCurrentStage.text = productDto.stage.name
        tvProductCodeResult.text = productDto.orderNumber
        tvProductInvoice.text = productDto.invoiceNumber
        tvProductName.text = productDto.name
        tvProductImageUrl.text = productDto.imageUlr
        Linkify.addLinks(tvProductImageUrl, Linkify.WEB_URLS)
    }
}