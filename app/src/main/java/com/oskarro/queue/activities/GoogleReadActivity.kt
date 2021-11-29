package com.oskarro.queue.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.oskarro.queue.R
import com.oskarro.queue.google.SpreadSheetDataSource
import org.json.JSONArray
import org.json.JSONObject

class GoogleReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_read)

        Log.d("PYK", "OOOOOO")
//        val entries = SpreadSheetDataSource()
//        val listAll = entries.listAllFake() // <--- replaced here
//        listAll.forEach { println(it) }
//        Log.d("PYK", listAll.toString())

        val url: String = "https://script.google.com/macros/s/AKfycbx6GbiYm3055KO_QB3pbJekwrP2GGu1Itbeh2iQCd2VfQFnTY3qeHscVIq6v-D_ulp9/exec"
        val stringRequest = object: StringRequest(
            Method.GET, url,
            Response.Listener {
                Toast.makeText(this@GoogleReadActivity, "TEST", Toast.LENGTH_SHORT).show()
                Log.d("DEEJAY1", it.toString())
            },
            Response.ErrorListener {
                Toast.makeText(this@GoogleReadActivity, "TEST", Toast.LENGTH_SHORT).show()
            }
        ) {
        }
        val queue = Volley.newRequestQueue(this@GoogleReadActivity)
        queue.add(stringRequest)


    }
}