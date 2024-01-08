package com.mejorappt.equipoa.util

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.method.LinkMovementMethodCompat
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.activities.currentProgress
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt


const val RESULTS_EXTRA_MESSAGE = "RESULTS"
const val SEE_FACTORS_EXTRA_MESSAGE = "SEE_FACTORS"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityBase(title: String = "", subTitle: String, menuIconImage: ImageVector? = null, iconTint: Color = Color.White, topAppBarBg: Color = Purple80, containerColor: Color = onSecondary_alt, navigationIconOnClick: () -> Unit = {}, iconImage: @Composable (() -> Unit)? = null, iconOnclick: () -> Unit = {}, scrollContent: @Composable (PaddingValues) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = containerColor,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.background(topAppBarBg, RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
                title = {
                        Column {
                            Text(
                                title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                fontSize = TextUnit(18F, TextUnitType.Sp),
                                fontFamily = FontFamily(Font(resId = R.font.lexend)),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                color = OnPrimary_alt
                            )

                            Text(
                                if (subTitle != "") "$subTitle ${stringResource(R.string.years_old)}" else "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                fontSize = TextUnit(12F, TextUnitType.Sp),
                                fontFamily = FontFamily(Font(resId = R.font.lexend)),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                color = OnPrimary_alt
                            )
                        }
                },
                navigationIcon = {
                    if (menuIconImage != null) {
                        IconButton(onClick = navigationIconOnClick) {
                            Icon(
                                imageVector = menuIconImage,
                                contentDescription = "Top app bar menu icon",
                                tint = iconTint
                            )
                        }
                    }
                },
                actions = {
                    if (iconImage != null) {
                        IconButton(onClick = iconOnclick) {
                            iconImage()
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        scrollContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityBase(title: String = "", menuIconImage: ImageVector? = null, iconTint: Color = Color.White, topAppBarBg: Color = Purple80, containerColor: Color = onSecondary_alt, navigationIconOnClick: () -> Unit = {}, iconImage: @Composable (() -> Unit)? = null, iconOnclick: () -> Unit = {}, scrollContent: @Composable (PaddingValues) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = containerColor,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.background(topAppBarBg, RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
                title = {
                    Column {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            fontSize = TextUnit(18F, TextUnitType.Sp),
                            fontFamily = FontFamily(Font(resId = R.font.lexend)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            color = OnPrimary_alt
                        )
                    }
                },
                navigationIcon = {
                    if (menuIconImage != null) {
                        IconButton(onClick = navigationIconOnClick) {
                            Icon(
                                imageVector = menuIconImage,
                                contentDescription = "Top app bar menu icon",
                                tint = iconTint
                            )
                        }
                    }
                },
                actions = {
                    if (iconImage != null) {
                        IconButton(onClick = iconOnclick) {
                            iconImage()
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        scrollContent(innerPadding)
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, @StringRes message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



@Composable
fun RadioGroup(radioOptions: List<String>, onClick: (Int?) -> Unit, selectedItem: String? = null) {
    val (selectedOption, onOptionSelected) = rememberSaveable {
        mutableStateOf(
            if (selectedItem != null && radioOptions.contains(selectedItem)) {
                selectedItem
            } else {
                null
            }
        )
    }

    Row(
        Modifier
            .selectableGroup()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        radioOptions.forEach { text ->
            RadioButton(
                selected = (text == selectedOption),
                onClick = {

                    if (selectedItem == null) {
                        currentProgress += 0.05f
                    }
                    onOptionSelected(text)
                    onClick(text.toIntOrNull())
                }
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun Question(text: String, onClick: (Int?) -> Unit, selectedItem: String? = null, error: Boolean = false) {
    val radioOptions = listOf("0","1","2","3","4")
    Card (modifier = Modifier.padding(10.dp)){
        Text(text = text, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .align(Alignment.CenterHorizontally),
            color = if (error) Color.Red else Color.Black)
        RadioGroup(radioOptions = radioOptions, onClick = onClick, selectedItem = selectedItem)
    }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val currentContext = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.apply {
            text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            movementMethod = LinkMovementMethodCompat.getInstance()
            typeface = ResourcesCompat.getFont(currentContext, R.font.lexend)
        }}
    )
}

@Composable
fun DropDownCard(title: String, maxHeight: Float = 200f, expandedContent: @Composable (BoxScope.() -> Unit)) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val heightState = animateFloatAsState(
        targetValue = if (expanded) maxHeight else 50f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(heightState.value.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Always on display text
            Text(
                text = title,
                modifier = Modifier.padding(16.dp)
            )

            // Drop down content
            Box(
                modifier = Modifier
                    .height(heightState.value.dp)
                    .padding(16.dp)
            ) {
                if (expanded) {
                    expandedContent(this)
                }
            }
        }
    }
}