package com.mejorappt.equipoa.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.db.ResultDAO
import com.mejorappt.equipoa.ui.theme.MejorApptTheme
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt
import com.mejorappt.equipoa.userProfile
import com.mejorappt.equipoa.util.ActivityBase
import com.mejorappt.equipoa.util.DropDownCard
import com.mejorappt.equipoa.util.RESULTS_EXTRA_MESSAGE
import com.mejorappt.equipoa.util.ResultCalculator
import com.mejorappt.equipoa.util.SEE_FACTORS_EXTRA_MESSAGE
import java.text.DateFormat

class PreviousResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultDAO = ResultDAO(this)

        val results = resultDAO.getAll(userProfile.id)

        setContent {
            MejorApptTheme {
                ActivityBase (title = getString(R.string.title_activity_previous_results), menuIconImage = Icons.Rounded.ArrowBack,
                    navigationIconOnClick = { navigateUpTo(intent) },
                    iconTint = OnPrimary_alt,
                    topAppBarBg = onSecondary_alt,
                    containerColor = Purple80) {

                    Image(painter = painterResource(id = R.drawable.home), contentDescription = "Landscape",
                        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)


                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {

                        Column (modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = if (results.isNotEmpty()) Arrangement.Top else Arrangement.Center) {


                            if (results.isNotEmpty()) {
                                results.forEach { result ->
                                    val factors = ResultCalculator.getSumFactors(result)
                                    val levels = ResultCalculator.calculateResult(factors)

                                    DropDownCard (title = "${getString(R.string.result)} ${result.formatDate(dateStyle = DateFormat.MEDIUM)}") {

                                        Column {

                                            Factors(levels = levels)

                                        }

                                        Button(onClick = {
                                            val intent = Intent(this@PreviousResultsActivity, AdvicesActivity::class.java).apply {
                                                putExtra(RESULTS_EXTRA_MESSAGE, levels)
                                                putExtra(SEE_FACTORS_EXTRA_MESSAGE, false)
                                            }

                                            startActivity(intent)
                                        }, modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(onSecondary_alt, OnPrimary_alt)
                                        ) {
                                            Icon(imageVector = Icons.Rounded.List, contentDescription = "Rounded Icon", modifier = Modifier.padding(end = 10.dp))
                                            Text(text = getString(R.string.see_advices))
                                        }
                                    }

                                }
                            } else {
                                Text(
                                    text = getString(R.string.there_are_no_previous_results),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .align(Alignment.CenterHorizontally),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Factors(levels: Array<String>) {
        Text(text = "${getString(R.string.pysh_factor)}: ${getLeveLStringLocale(levels[0])}")
        Text(text = "${getString(R.string.cog_factor)}: ${getLeveLStringLocale(levels[1])}")
        Text(text = "${getString(R.string.avoid_factor)}: ${getLeveLStringLocale(levels[2])}")
    }

    private fun getLeveLStringLocale(level: String) :String {
        return when (level) {
            ResultCalculator.HIGH -> getString(R.string.high)
            ResultCalculator.MEDIUM -> getString(R.string.medium)
            ResultCalculator.LOW -> getString(R.string.low)
            else -> getString(R.string.how)
        }
    }
}