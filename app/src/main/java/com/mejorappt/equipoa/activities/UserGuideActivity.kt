package com.mejorappt.equipoa.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
            val listState = rememberLazyListState()

            MejorApptTheme {
                ActivityBase (title = stringResource(id = R.string.title_activity_user_guide), menuIconImage = Icons.Rounded.ArrowBack,
                    navigationIconOnClick = { navigateUpTo(intent) },
                    iconTint = OnPrimary_alt,
                    topAppBarBg = onSecondary_alt,
                    containerColor = Purple80
                ) {
                    Image(painter = painterResource(id = R.drawable.background_user_guide), contentDescription = "Landscape",
                        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)

                    LazyColumn (modifier = Modifier
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = it,
                        state = listState) {
                        item {
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
                        }

                        item {
                            DropDownCard(title = getString(R.string.users_title_guide)) {
                                Divider()
                                Text(text = buildString {
                                    append(getString(R.string.header_users_guide_1))
                                    append(getString(R.string.header_users_guide_2))
                                    append(getString(R.string.header_users_guide_3))
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
}