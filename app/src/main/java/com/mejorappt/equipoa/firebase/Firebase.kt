package com.mejorappt.equipoa.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface Firebase<E> {
    val db: FirebaseFirestore
        get() = Firebase.firestore

    fun insert(data: E)

    fun update(data: E)

    fun synchronize()
}