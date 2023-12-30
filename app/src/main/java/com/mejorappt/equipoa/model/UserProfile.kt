package com.mejorappt.equipoa.model

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.enums.Gender

data class UserProfile(val id: Int, val userName:String, val age:Int, val gender: Gender, @DrawableRes val happyIcon: Int, @DrawableRes val annoyedIcon:Int) {

    @Composable
    fun HappyIcon(mirrored: Boolean = false) {
        var modifier = Modifier.height(100.dp).width(100.dp)
        if (mirrored) {
            modifier = modifier.graphicsLayer(scaleX = -1f)
        }

        Image(painter = painterResource(id = happyIcon), contentDescription = "Happy icon", contentScale = ContentScale.Fit, modifier = modifier)
    }

    @Composable
    fun AnnoyedIcon(mirrored: Boolean = false) {
        var modifier = Modifier.height(100.dp).width(100.dp)
        if (mirrored) {
            modifier = modifier.graphicsLayer(scaleX = -1f)
        }

        Image(painter = painterResource(id = annoyedIcon), contentDescription = "Annoyed icon", contentScale = ContentScale.Fit, modifier = modifier)
    }

    fun userExists(): Boolean {
        return userName.isNotBlank()
    }

    companion object {
        val EMPTY_USER = UserProfile(0, "", 0, Gender.NON_BINARY, R.drawable.non_binary_icon_happy, R.drawable.non_binary_icon_annoyed)
    }
}
