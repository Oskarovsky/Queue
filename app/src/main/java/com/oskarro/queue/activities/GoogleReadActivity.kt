package com.oskarro.queue.activities

import android.content.Intent
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
import com.oskarro.queue.adapters.ProductRowsAdapter
import com.oskarro.queue.firebase.FirebaseUtils
import com.oskarro.queue.model.ProductDto
import com.oskarro.queue.model.Sheet
import com.oskarro.queue.model.Stage
import com.oskarro.queue.utils.Constants
import com.oskarro.queue.utils.SheetValues
import com.oskarro.queue.utils.SheetValues.MULTI_REQUEST
import kotlinx.android.synthetic.main.activity_google_read.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product_row_details.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_content.*
import org.json.JSONObject

class GoogleReadActivity : BaseActivity() {

    private var requestQueue: RequestQueue? = null

    private lateinit var sheetObject: Sheet

    val arrayProducts: ArrayList<ProductDto> = ArrayList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_read)

        setupActionBar()
        FirebaseUtils().loadCurrentSheetUrl(this@GoogleReadActivity)

        showProgressDialog(resources.getString(R.string.please_wait))
        requestQueue = Volley.newRequestQueue(this@GoogleReadActivity)
//        fetchDataFromSheet()
    }

    fun fetchDataFromSheet() {
        val url:String = Constants.GOOGLE_SCRIPT
        val stringRequest = object: StringRequest(
            Method.GET,
            url.plus("?sheetTabName=${sheetObject.tabName}&sheetUrl=${sheetObject.url}"),
            Response.Listener { response ->
                val jsonObj = JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1))
                val productJson = jsonObj.getJSONArray(SheetValues.PRODUCTS)
                for (i in 0..productJson.length() - 1) {
                    val dto = ProductDto()
                    dto.stage = Stage.fromString(productJson.getJSONObject(i).getString(SheetValues.STAGE))
                    dto.invoiceNumber = productJson.getJSONObject(i).getString(SheetValues.INVOICE_NUMBER)
                    dto.orderNumber = productJson.getJSONObject(i).getString(SheetValues.CODE)
                    dto.name = productJson.getJSONObject(i).getString(SheetValues.NAME)
                    dto.status = productJson.getJSONObject(i).getString(SheetValues.STATUS)
                    arrayProducts.add(dto)
                }
                populateProductsListToUI(
                    arrayProducts
                        .filter { it.stage != Stage.KONIEC }
                        .filter { it.status == Constants.INITIALIZED }
                        .sortedBy { it.stage.ordinal })
                hideProgressDialog()
            },
            Response.ErrorListener {
                Toast.makeText(this@GoogleReadActivity, "Error has occurred!", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["requestMethod"] = MULTI_REQUEST
                return params
            }

        }
        requestQueue?.add(stringRequest)
    }


    fun populateProductsListToUI(productsList: List<ProductDto>) {
        if (productsList.size > 0) {
            rv_row_products_list.visibility = View.VISIBLE
            tv_no_row_products.visibility = View.GONE

            rv_row_products_list.layoutManager = LinearLayoutManager(this)
            rv_row_products_list.setHasFixedSize(true)

            val adapter = ProductRowsAdapter(this, productsList)
            rv_row_products_list.adapter = adapter

            adapter.setOnClickListener(object: ProductRowsAdapter.OnClickListener{
                override fun onClick(position: Int, model: ProductDto) {
                    val intent = Intent(this@GoogleReadActivity, ProductRowDetailsActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_ORDER_NUMBER, model.orderNumber)
                    intent.putExtra(Constants.PRODUCT_INVOICE_NUMBER, model.invoiceNumber)
                    startActivity(intent)
                }
            })
        } else {
            rv_row_products_list.visibility = View.GONE
            tv_no_row_products.visibility = View.VISIBLE
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_read_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24p)
            actionBar.title = resources.getString(R.string.product_list_board_title)
        }
        toolbar_product_read_activity.setNavigationOnClickListener{
            startActivity(Intent(this@GoogleReadActivity, GoogleActivity::class.java))
        }
    }

    fun setSheetUrlForRequest(sheet: Sheet) {
        sheetObject = sheet
    }

}