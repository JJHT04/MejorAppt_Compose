package com.mejorappt.equipoa.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.ui.theme.MejorApptTheme
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt
import com.mejorappt.equipoa.util.ActivityBase
import com.mejorappt.equipoa.util.DropDownCard

class UserGuideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MejorApptTheme {
                ActivityBase (title = stringResource(id = R.string.title_activity_user_guide), menuIconImage = Icons.Rounded.ArrowBack,
                    navigationIconOnClick = { navigateUpTo(intent) },
                    iconTint = OnPrimary_alt,
                    topAppBarBg = onSecondary_alt,
                    containerColor = Purple80
                ) {
                    Image(painter = painterResource(id = R.drawable.home), contentDescription = "Landscape",
                        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)

                    Column (modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(
                            rememberScrollState()
                        ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        DropDownCard(title = stringResource(id = R.string.title_activity_test)) {
                            Divider()
                            Text(
                                "${getString(R.string.test_user_guide_header)}:\n" +
                                    "-${stringResource(id = R.string.never)}\n" +
                                    "-${stringResource(id = R.string.few_times)}\n" +
                                    "-${stringResource(id = R.string.sometimes)}\n" +
                                    "-${stringResource(id = R.string.frequently)}\n" +
                                    "-${stringResource(id = R.string.always)}\n" +
                                    getString(R.string.test_user_guide_footer),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(15.dp))
                        }
                        
                        DropDownCard(title = "Users") {
                            Divider()
                            Text(text = buildString {
                                append("If you click the three lines on the top left corner, you will see the users menu ")
                                append("where you can add, edit and see the list of users in your device.\n")
                                append("There is no need to create an user, the app will submit your test results as an anonymous user.")
                            },
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(15.dp))
                        }
                    }
                }
            }
        }
    }
}