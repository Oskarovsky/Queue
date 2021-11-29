package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.oskarro.queue.R
import com.oskarro.queue.google.SpreadSheetDataSource
import com.oskarro.queue.model.ProductDto
import com.oskarro.queue.model.ProductResponse
import org.json.JSONArray
import org.json.JSONObject

class GoogleReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_read)

        val url = "https://script.google.com/macros/s/AKfycbwRQ0bYKpPifDY_bMyCXl16iJczH1AMeRuGpJBvNd_hxKfctI7Axwor7oCKaCOeyExt/exec"
        val stringRequest = object: StringRequest(
            Method.GET, url,
            Response.Listener {
                Log.d("DEEJAY1", it.toString())
                val foos = ProductResponse(it.toString())
                Log.d("DEEJAY2", foos.toString())

                val jsonObj = JSONObject(it.substring(it.indexOf("{"), it.lastIndexOf("}") + 1))
                val productJson = jsonObj.getJSONArray("products")
                for (i in 0..productJson.length() - 1) {
                    val dto = ProductDto()
                    dto.stage = productJson.getJSONObject(i).getString("productStatus")
                    dto.orderNumber = productJson.getJSONObject(i).getString("productCode")
                    dto.name = productJson.getJSONObject(i).getString("productName")
                    Log.d("DEEJAY3", dto.toString())
                }

            },
            Response.ErrorListener {
                Toast.makeText(this@GoogleReadActivity, "ERROR TEST", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["requestMethod"] = "MULTI" // TODO
                return params
            }
        }
        val queue = Volley.newRequestQueue(this@GoogleReadActivity)
        queue.add(stringRequest)
        Log.d("DEEJAY_XXX", stringRequest.toString())
    }
}