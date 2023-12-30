package com.mejorappt.equipoa.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.ui.theme.MejorApptTheme
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt
import com.mejorappt.equipoa.util.ActivityBase
import com.mejorappt.equipoa.util.RESULTS_EXTRA_MESSAGE
import com.mejorappt.equipoa.util.ResultCalculator.Advices
import com.mejorappt.equipoa.util.SEE_FACTORS_EXTRA_MESSAGE

class AdvicesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        val results = intent.getStringArrayExtra(RESULTS_EXTRA_MESSAGE)

        setContent {
            MejorApptTheme {
                ActivityBase (title = stringResource(id = R.string.title_activity_advices), menuIconImage = Icons.Rounded.ArrowBack,
                    navigationIconOnClick = {
                        setResult(Activity.RESULT_OK, intent); finish()},
                    iconTint = OnPrimary_alt,
                    topAppBarBg = onSecondary_alt,
                    containerColor = Purple80) {

                    Image(painter = painterResource(id = R.drawable.home), contentDescription = "Landscape",
                        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {
                        if (results != null) {
                            Advices(results, seeFactors = intent.getBooleanExtra(
                                SEE_FACTORS_EXTRA_MESSAGE, true))
                        }
                    }


                }
            }
        }
    }
}