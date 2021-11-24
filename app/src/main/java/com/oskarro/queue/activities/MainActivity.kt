package com.oskarro.queue.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.oskarro.queue.databinding.ActivityMainBinding

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