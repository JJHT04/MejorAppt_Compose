package com.mejorappt.equipoa.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.enums.Gender
import com.mejorappt.equipoa.model.UserProfile
import com.mejorappt.equipoa.userProfile
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "USERS")

val USER_ID_KEY = intPreferencesKey("userID")
val USER_NAME_KEY = stringPreferencesKey("userName")
val AGE_KEY = intPreferencesKey("age")
val GENDER_KEY = stringPreferencesKey("gender")
val HAPPY_ICON_KEY = intPreferencesKey("happyIcon")
val ANNOYED_ICON_KEY = intPreferencesKey("annoyedIcon")

object DataStoreUtil {
    suspend fun saveData(context: Context, id: Int ,userName: String, age:Int, gender: Gender, @DrawableRes happyIcon: Int, @DrawableRes annoyedIcon: Int) {
        context.dataStore.edit {
            it[USER_ID_KEY] = id
            it[USER_NAME_KEY] = userName
            it[AGE_KEY] = age
            it[GENDER_KEY] = gender.name
            it[HAPPY_ICON_KEY] = happyIcon
            it[ANNOYED_ICON_KEY] = annoyedIcon
        }
    }

    suspend fun saveData(context: Context, @DrawableRes happyIcon: Int, @DrawableRes annoyedIcon: Int) {
        context.dataStore.edit {
            it[HAPPY_ICON_KEY] = happyIcon
            it[ANNOYED_ICON_KEY] = annoyedIcon
        }
    }

    suspend fun saveData(context: Context, userName: String, age:Int, gender: Gender) {
        context.dataStore.edit {
            it[USER_ID_KEY] = userProfile.id
            it[USER_NAME_KEY] = userName
            it[AGE_KEY] = age
            it[GENDER_KEY] = gender.name
            it[HAPPY_ICON_KEY] = userProfile.happyIcon
            it[ANNOYED_ICON_KEY] = userProfile.annoyedIcon
        }
    }

    fun getData(context: Context) = context.dataStore.data.map { preferences ->
        UserProfile(preferences[USER_ID_KEY]?:0, preferences[USER_NAME_KEY].orEmpty(), preferences[AGE_KEY]?:0, Gender.valueOf(preferences[GENDER_KEY]?:Gender.NON_BINARY.name), preferences[HAPPY_ICON_KEY]?:R.drawable.female_icon_happy, preferences[ANNOYED_ICON_KEY]?:R.drawable.female_icon_annoyed)
    }
}