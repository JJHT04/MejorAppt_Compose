package com.mejorappt.equipoa.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Result(val id: Int, val userName: UserProfile?, val date: Date, val factor1: Int, val factor2: Int, val factor3: Int, private val uploaded: Boolean) {

    fun formatDate(): String = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)

    fun formatDate(dateStyle: Int, timeStyle: Int = DateFormat.MEDIUM): String = SimpleDateFormat.getDateTimeInstance(dateStyle, timeStyle).format(date)

    fun getUploaded(): Int = if (uploaded) 1 else 0

    companion object {
        fun parseDate(str: String): Date? = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(str)
    }

}
