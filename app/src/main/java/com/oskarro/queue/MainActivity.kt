package com.oskarro.queue

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.oskarro.queue.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val TAG = "MyMessage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uploadData()
    }

    private fun uploadData() {
        binding!!.btnUploadData.setOnClickListener {

            // create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "code" to "XC12",
                "description" to "some information about process",
                "amount" to 10,
                "stage" to "release"
            )

            // use the add() method to create a document inside users collection
            FirebaseUtils().fireStoreDatabase.collection("processes")
                .add(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "Added document with ID ${it.id}")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error adding document $exception")
                }
        }
    }

    private fun readData() {
        binding!!.btnReadData.setOnClickListener {
            FirebaseUtils().fireStoreDatabase.collection("users")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Log.d(TAG, "Read document with ID ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents $exception")
                }
        }
    }


}