package com.mejorappt.equipoa.firebase

import android.content.Context
import android.util.Log
import com.mejorappt.equipoa.db.ResultDAO
import com.mejorappt.equipoa.model.Result

class FirebaseResult(private val context: Context): Firebase<Result> {
    override fun insert(data: Result) {
        db.collection("results").add(data.map())
            .addOnSuccessListener {
                Log.d("TAG_DATA", "DocumentSnapshot added with ID: ${data.id}")
                val resultDAO = ResultDAO(context)
                data.setUploaded(true)
                resultDAO.update(data)
            }
            .addOnFailureListener {
                Log.w("TAG_DATA2", "Error adding document", it)
            }
    }

    override fun update(data: Result) {
        //NOT NEEDED FOR RESULTS
    }

    override fun synchronize() {
        val resultDAO = ResultDAO(context)
        val results = resultDAO.getAsync()

        results.forEach {

            insert(it)

        }
    }
}