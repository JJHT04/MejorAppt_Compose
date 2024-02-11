package com.mejorappt.equipoa.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.db.QuestionDAO
import com.mejorappt.equipoa.db.ResultDAO
import com.mejorappt.equipoa.firebase.FirebaseResult
import com.mejorappt.equipoa.model.Question
import com.mejorappt.equipoa.model.Result
import com.mejorappt.equipoa.testStarted
import com.mejorappt.equipoa.ui.theme.MejorApptTheme
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt
import com.mejorappt.equipoa.userProfile
import com.mejorappt.equipoa.util.ActivityBase
import com.mejorappt.equipoa.util.MultipleLinearProgressIndicator
import com.mejorappt.equipoa.util.Question
import com.mejorappt.equipoa.util.RESULTS_EXTRA_MESSAGE
import com.mejorappt.equipoa.util.ResultCalculator
import com.mejorappt.equipoa.util.getValuesMap
import kotlinx.coroutines.launch
import java.util.Date

var currentProgress by mutableFloatStateOf(0.15f)
class TestActivity : ComponentActivity() {

    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

        setContent {
            val snackBarHostState = remember { SnackbarHostState() }

            MejorApptTheme {

                ActivityBase(title = stringResource(id = R.string.title_activity_test), menuIconImage = Icons.Rounded.ArrowBack,
                    navigationIconOnClick = {
                        navigateUpTo(intent) },
                    iconTint = OnPrimary_alt,
                    topAppBarBg = onSecondary_alt,
                    containerColor = Purple80,
                    snackBarHostState = snackBarHostState) { paddingValues ->

                    val questionDAO = QuestionDAO(this)
                    val nOfQuestionsAnswered = questionDAO.getNumberOfAnsweredQuestions()

                    currentProgress = (nOfQuestionsAnswered / 20f) * 0.85f + 0.15f

                    val questions = questionDAO.getAll()

                    Image(painter = painterResource(id = R.drawable.background_test), contentDescription = "Landscape",
                        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        val listState = rememberLazyListState()

                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f),
                            verticalArrangement = Arrangement.SpaceAround) {

                            Row (modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.1f)
                                .padding(top = 10.dp, start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                MultipleLinearProgressIndicator(
                                    primaryProgress = currentProgress,
                                    secondaryProgress = currentProgress + 0.05f,
                                    icon = painterResource(id = userProfile.happyIcon)
                                )
                            }

                            val firstQuestionUnchecked = getFirstUncheckedQuestion(questions)
                            LaunchedEffect(key1 = Unit) {
                                if (firstQuestionUnchecked != null) {
                                    listState.animateScrollToItem(index = firstQuestionUnchecked.id -1)
                                }
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.97f)
                                    .fillMaxHeight(0.95f)
                                    .background(onSecondary_alt, shape = RoundedCornerShape(20.dp))
                                    .padding(10.dp),
                                state = listState
                            ) {

                                items(questions.size) {i ->

                                    var actualValue by rememberSaveable { mutableIntStateOf(questions[i].answer) }

                                    Row {
                                        Question(text = "${i + 1}. ${questions[i].question}", error = actualValue == -1, onClick = {value ->
                                            if (value != null) {
                                                questions[i].answer = value
                                                actualValue = value
                                                questionDAO.update(questions[i])
                                                testStarted = true
                                            }
                                        }, selectedItem = if (questions[i].answer != -1) getValuesMap().entries.find { it.value == questions[i].answer }?.key else null)
                                    }

                                }
                            }
                        }

                        val scope = rememberCoroutineScope()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.19f)
                                .padding(10.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = {

                                        if (questionDAO.isAnyQuestionNotAnswered()) {
                                            scope.launch {
                                                listState.animateScrollToItem(index = (getFirstUncheckedQuestion(questions)?.id
                                                    ?: 1) -1)

                                                snackBarHostState.showSnackbar(getString(R.string.answer_all_the_questions))
                                            }

                                        } else {
                                            btnFinishOnClick(questions, questionDAO)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = onSecondary_alt, contentColor = OnPrimary_alt),
                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                ) {
                                    Icon(imageVector = Icons.Rounded.Done, contentDescription = "Icon", modifier = Modifier.padding(end = 10.dp))
                                    Text(text = getString(R.string.finish), fontSize = 20.sp)
                                }
                            }
                        }
                    }

                }

            }
        }
    }

    private fun getFirstUncheckedQuestion(questions: List<Question>): Question? {
        questions.forEach {
            if (it.answer == -1) {
                return it
            }
        }

        return null
    }

    private fun btnFinishOnClick(questions: List<Question>, questionDAO: QuestionDAO) {

        val resultDAO = ResultDAO(this)

        val sumFactors = getSumFactors(questions)

        val result = Result(resultDAO.getMaxId(), userProfile, Date(), sumFactors[0], sumFactors[1], sumFactors[2], false)

        resultDAO.insert(result)

        FirebaseResult(this).insert(result)

        val levels = ResultCalculator.calculateResult(sumFactors)

        val intent = Intent(this, AdvicesActivity::class.java).apply {
            putExtra(RESULTS_EXTRA_MESSAGE, levels)
        }

        questionDAO.clearAll()

        testStarted = false
        currentProgress = 0.25f

        someActivityResultLauncher.launch(intent)
    }

    private fun getSumFactors(questions: List<Question>): Array<Int> {
        val sumFactors = intArrayOf(0,0,0)

        questions.forEach {
            when (it.factor) {
                1 -> sumFactors[0] += it.answer
                2 -> sumFactors[1] += it.answer
                3 -> sumFactors[2] += it.answer
            }
        }

        return sumFactors.toTypedArray()
    }
}