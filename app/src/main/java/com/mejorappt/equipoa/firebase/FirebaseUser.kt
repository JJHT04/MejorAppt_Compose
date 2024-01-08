package com.mejorappt.equipoa.firebase

import android.content.Context
import android.util.Log
import com.mejorappt.equipoa.db.UserDAO
import com.mejorappt.equipoa.model.UserProfile

class FirebaseUser(private val context: Context): Firebase<UserProfile> {
    override fun insert(data: UserProfile) {
        db.collection("users").document("${data.id}_${data.userName}_${data.formatDate()}").set(data.map())
            .addOnSuccessListener {
                Log.d("TAG_DATA", "DocumentSnapshot added with ID: ${data.id}_${data.userName}_${data.formatDate()}")
                data.uploaded = 1
                val userDAO = UserDAO(context)
                userDAO.update(data)
            }
            .addOnFailureListener {
                Log.w("TAG_DATA2", "Error adding document", it)
            }
    }

    override fun update(data: UserProfile) {

        val docRef = db.collection("users").document("${data.id}_${data.userName}_${data.formatDate()}")

        db.runTransaction { transition ->

            transition.update(docRef, data.map())

            null
        }.addOnSuccessListener {
            Log.d("TAG1", "Transaction success!")
            data.uploaded = 1
            val userDAO = UserDAO(context)
            userDAO.update(data)
        }
            .addOnFailureListener { e -> Log.w("TAG2", "Transaction failure.", e) }

    }

    override fun synchronize() {
        val userDAO = UserDAO(context)
        val users = userDAO.getAsync()

        users.forEach {
            if (it.uploaded == 0) {
                insert(it)
            } else if (it.uploaded == 3) {
                update(it)
            }
        }
    }
}