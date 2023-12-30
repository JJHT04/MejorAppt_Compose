package com.mejorappt.equipoa.util

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.model.Advice
import com.mejorappt.equipoa.model.Result

object ResultCalculator {
    const val MEDIUM = "medium"
    const val HIGH = "high"
    const val LOW = "low"

    fun getSumFactors(result: Result): Array<Int> {
        return arrayOf(result.factor1, result.factor2, result.factor3)
    }

    @Composable
    private fun Advice(index: Int, advices: List<Advice>) {
        val i = index - 1

        Card (modifier = Modifier.padding(10.dp)) {
            HtmlText(html = advices[i].adviceTitle, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp))
            Divider()
            HtmlText(html = advices[i].adviceBody, modifier = Modifier
                .fillMaxSize()
                .padding(20.dp))
        }
    }


    @Composable
    fun Advices(levels: Array<String>, fraction: Float = 1f, seeFactors: Boolean = true) {
        val advices = CSV.getAdvices(LocalContext.current)

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(fraction),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            if (seeFactors) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    ) {
                    Factors(levels = levels, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp))
                }
            }

            val fis = levels[0] == MEDIUM || levels[0] == HIGH
            val cog = levels[1] == MEDIUM || levels[1] == HIGH
            val evi = levels[2] == MEDIUM || levels[2] == HIGH
            val all = levels[3] == MEDIUM || levels[3] == HIGH
            val fisAndCog = levels[4] == MEDIUM || levels[4] == HIGH
            val cogAndEvi = levels[5] == MEDIUM || levels[5] == HIGH
            val eviAndFis = levels[6] == MEDIUM || levels[6] == HIGH
            val none = levels[3] == LOW

            if (fis || evi || all || fisAndCog || cogAndEvi || eviAndFis) {
                Advice(6, advices)
            }

            if (fis) {
                Advice(7, advices)
            }

            if (fis || all || fisAndCog || eviAndFis) {
                Advice(8, advices)
            }

            if (fis || fisAndCog) {
                Advice(9, advices)
            }

            if (cog) {
                Advice(1, advices)
                Advice(4, advices)
                Advice(5, advices)
            }

            if (cog || fisAndCog) {
                Advice(2, advices)
            }

            if (cog || evi || fisAndCog || cogAndEvi) {
                Advice(3, advices)
            }

            if (evi || all || cogAndEvi || eviAndFis) {
                Advice(11, advices)
            }

            if (all) {
                Advice(12, advices)
            }

            if (none) {
                TextDivider(text = stringResource(R.string.advices_to_get_all_under_control))
            } else {
                TextDivider(text = stringResource(R.string.advices_to_not_get_to_this_situation))
            }

            if (fis || cog || all || fisAndCog || cogAndEvi || eviAndFis || none) {
                Advice(13, advices)
                Advice(14, advices)
                Advice(15, advices)
                if (fis || all || cogAndEvi || eviAndFis) {
                    Advice(10, advices)
                }

            }

            if (evi) {
                Advice(19, advices)
                Advice(20, advices)
            }

            if ((evi || cog || fisAndCog) && !(fis || all || cogAndEvi || eviAndFis)) {
                Advice(10, advices)
            }
        }
    }

    @Composable
    private fun TextDivider(text: String) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
            Text(text = text, textAlign = TextAlign.Center, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), fontSize = 20.sp)
        }
    }


    private fun getVariables(factor: Int): Pair<Int, Int> {
        var x = 0
        var y = 0
        if (factor == 1) {
            x = 14
            y = 24
        }
        if (factor == 2) {
            x = 14
            y = 22
        }
        if (factor == 3) {
            x = 1
            y = 4
        }
        return Pair(x, y)
    }

    private fun getVariables2(factor: Int): Pair<Int, Int> {
        var x = 0
        var y = 0
        if (factor == 0) {
            x = 31
            y = 50
        }
        if (factor == 1) {
            x = 29
            y = 46
        }
        if (factor == 2) {
            x = 16
            y = 27
        }
        if (factor == 3) {
            x = 16
            y = 29
        }
        return Pair(x, y)
    }

    fun calculateResult(factors: Array<Int>): Array<String> {

        var x: Int
        var y: Int

        // Almacena los niveles de los factores por separado
        val level = arrayOf("", "", "")
        // Almacena los niveles de las sumas de los factores
        val level2 = arrayOf("", "", "", "")

        for ((t, i) in (1..3).withIndex()) {
            val variables = getVariables(i)
            x = variables.first
            y = variables.second

            if (factors[t] <= x) {
                level[t] = LOW
            }
            if (factors[t] in (x + 1)..y) {
                level[t] = MEDIUM
            }
            if (factors[t] > y) {
                level[t] = HIGH
            }
        }

        /*
        sumFactores2[0]: suma de todos
        sumFactores2[1]:suma Fisiologico y Cognitivo
        sumFactores2[2]: suma Cognitivo y Evitacion
        sumFactores2[3]: suma Fisiologico y evitcion
         */

        val sumFactors2 = arrayOf(
            factors[0] + factors[1] + factors[2],
            factors[0] + factors[1], factors[1] + factors[2], factors[0] + factors[2]
        )

        for (j in sumFactors2.indices) {
            val variables = getVariables2(j)
            x = variables.first
            y = variables.second

            if (sumFactors2[j] <= x) {
                level2[j] = LOW
            }
            if (sumFactors2[j] in (x + 1)..y) {
                level2[j] = MEDIUM
            }
            if (sumFactors2[j] > y) {
                level2[j] = HIGH
            }
        }

        // Almacena todos los niveles, los tres primeros son en separado, el resto son las sumas
        return arrayOf(level[0], level[1], level[2], level2[0], level2[1], level2[2], level2[3])
    }

    @Composable
    private fun Factors(modifier: Modifier = Modifier, levels: Array<String>) {
        val context = LocalContext.current
        Text(text = stringResource(R.string.results), modifier = modifier, textAlign = TextAlign.Center)
        Divider()
        Text(text = "${context.getString(R.string.pysh_factor)}: ${getLeveLStringLocale(levels[0], context)}", modifier = modifier, textAlign = TextAlign.Center)
        Text(text = "${context.getString(R.string.cog_factor)}: ${getLeveLStringLocale(levels[1], context)}", modifier = modifier, textAlign = TextAlign.Center)
        Text(text = "${context.getString(R.string.avoid_factor)}: ${getLeveLStringLocale(levels[2], context)}", modifier = modifier, textAlign = TextAlign.Center)
    }

    private fun getLeveLStringLocale(level: String, context: Context) :String {
        return when (level) {
            HIGH -> context.getString(R.string.high)
            MEDIUM -> context.getString(R.string.medium)
            LOW -> context.getString(R.string.low)
            else -> context.getString(R.string.how)
        }
    }
}