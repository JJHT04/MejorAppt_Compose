package com.mejorappt.equipoa.enums

import android.content.Context
import com.mejorappt.equipoa.R

enum class Gender {

    MALE {
        override fun toString(context: Context): String {
            return context.getString(R.string.male)
        }

        override fun toString(): String {
            return MALE_GENDER_DEFAULT_STRING
        }
    },
    FEMALE {
        override fun toString(context: Context): String {
            return context.getString(R.string.female)
        }

        override fun toString(): String {
            return FEMALE_GENDER_DEFAULT_STRING
        }
    },
    NON_BINARY {
        override fun toString(context: Context): String {
            return context.getString(R.string.non_binary)
        }

        override fun toString(): String {
            return NON_BINARY_GENDER_DEFAULT_STRING
        }
    };

    abstract fun toString(context: Context): String

    companion object {
        const val FEMALE_GENDER_DEFAULT_STRING = "Mujer"
        const val MALE_GENDER_DEFAULT_STRING = "Hombre"
        const val NON_BINARY_GENDER_DEFAULT_STRING = "No Binario"


        fun getLocaleStringRepresentations(context: Context): List<String> = entries.map { it.toString(context) }

        fun getGenderMap(context: Context): Map<String, Gender> {
            val map = mutableMapOf<String,Gender>()

            entries.forEach {
                map[it.toString(context)] = it
            }

            return map.toMap() //Asi solo devuelve un read-only map
        }

        fun getDefaultGenderMap(): Map<String, Gender> {
            val map = mutableMapOf<String,Gender>()

            entries.forEach {
                map[it.toString()] = it
            }

            return map.toMap()
        }
    }

}