package com.mejorappt.equipoa.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

data class Result(
    val id: Int,
    val userName: UserProfile?,
    val date: Date,
    val factor1: Int,
    val factor2: Int,
    val factor3: Int,
    private var uploaded: Boolean
) {

    fun formatDate(): String =
        SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)

    fun formatDate(dateStyle: Int, timeStyle: Int = DateFormat.MEDIUM, timeZone: TimeZone = TimeZone.getDefault()): String =
        SimpleDateFormat.getDateTimeInstance(dateStyle, timeStyle).apply {
            this.timeZone = timeZone
        }.format(date)

    fun getUploaded(): Int = if (uploaded) 1 else 0

    fun setUploaded(uploaded: Boolean) {
        this.uploaded = uploaded
    }

    fun map(): Map<String, Any> = hashMapOf(
        "user" to (userName?.map()?: UserProfile.EMPTY_USER.map()),
        "date" to formatDate(),
        "factor1" to factor1,
        "factor2" to factor2,
        "factor3" to factor3,
    )

    companion object {
        fun parseDate(str: String): Date? =
            SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(str)
    }

}
