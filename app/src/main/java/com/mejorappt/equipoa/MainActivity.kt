package com.mejorappt.equipoa

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.mejorappt.equipoa.activities.PreviousResultsActivity
import com.mejorappt.equipoa.activities.TestActivity
import com.mejorappt.equipoa.activities.UserGuideActivity
import com.mejorappt.equipoa.db.QuestionDAO
import com.mejorappt.equipoa.db.UserDAO
import com.mejorappt.equipoa.dialogs.ModifyDialog
import com.mejorappt.equipoa.dialogs.RegisterDialog
import com.mejorappt.equipoa.dialogs.UserListDialog
import com.mejorappt.equipoa.firebase.FirebaseResult
import com.mejorappt.equipoa.firebase.FirebaseUser
import com.mejorappt.equipoa.model.UserProfile
import com.mejorappt.equipoa.ui.theme.MejorApptTheme
import com.mejorappt.equipoa.ui.theme.OnPrimary
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.Purple80
import com.mejorappt.equipoa.ui.theme.onSecondary_alt
import com.mejorappt.equipoa.util.ActivityBase
import com.mejorappt.equipoa.util.DataStoreUtil
import com.mejorappt.equipoa.util.showToast
import kotlinx.coroutines.launch

var nombreUsuario by mutableStateOf<String?>(null)
private var annoyed by mutableStateOf(false)
private var icon by mutableStateOf<@Composable (() -> Unit)?>(null)
private var titleName by mutableStateOf("")
private var subTitle by mutableStateOf("")
var userProfile by mutableStateOf(UserProfile.EMPTY_USER)
var testStarted by mutableStateOf(false)
val modifyUser = mutableStateOf(UserProfile.EMPTY_USER)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseUser(this).synchronize()
        FirebaseResult(this).synchronize()

        val questionDAO = QuestionDAO(this)
        testStarted = questionDAO.isAnyQuestionAnswered()

        lifecycleScope.launch {
            DataStoreUtil.getData(this@MainActivity).collect {
                if (it.userExists()) {
                    userProfile = it
                    nombreUsuario = userProfile.userName
                    titleName = userProfile.userName
                    subTitle = userProfile.age.toString()
                    icon =
                        @Composable { if (!annoyed) userProfile.HappyIcon(true) else userProfile.AnnoyedIcon(true) }
                }
            }
        }

        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            var showRegisterDialog by rememberSaveable { mutableStateOf(false) }
            val showModifyDialog = rememberSaveable { mutableStateOf(false) }
            var showLoginDialog by rememberSaveable { mutableStateOf(false) }
            val showBottomSheet = rememberSaveable { mutableStateOf(false) }
            var showUserListDialog by rememberSaveable { mutableStateOf(false) }
            var userListClickable by rememberSaveable { mutableStateOf(false) }

            MejorApptTheme {
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = Purple80,
                            drawerContentColor = OnPrimary
                        ) {
                            Text(
                                "${stringResource(id = R.string.welcome)} ${nombreUsuario ?: ""}!",
                                modifier = Modifier.padding(16.dp)
                            )
                            Divider()
                            NavigationDrawerItem(
                                text = getString(R.string.register_new_user),
                                iconImage = Icons.Rounded.Add
                            ) {
                                showRegisterDialog = true
                            }

                            NavigationDrawerItem(
                                text = getString(R.string.login),
                                iconImage = Icons.Rounded.AccountCircle
                            ) {
                                if (userProfile.userExists()) {
                                    showLoginDialog = true
                                    userListClickable = true
                                    showUserListDialog = true
                                } else {
                                    showToast(this@MainActivity, getString(R.string.not_login_message))
                                }
                            }

                            NavigationDrawerItem(
                                text = getString(R.string.modify_actual_user),
                                iconImage = Icons.Rounded.Edit
                            ) {
                                if (userProfile.userExists()) {
                                    userListClickable = true
                                    showUserListDialog = true
                                } else {
                                    showToast(this@MainActivity, getString(R.string.not_login_message))
                                }
                            }

                            NavigationDrawerItem(
                                text = getString(R.string.show_user_list),
                                iconImage = Icons.Rounded.List
                            ) {
                                if (userProfile.userExists()) {
                                    userListClickable = false
                                    showUserListDialog = true
                                } else {
                                    showToast(this@MainActivity, getString(R.string.not_login_message))
                                }
                            }
                        }
                    },
                    drawerState = drawerState
                ) {
                    ActivityBase(
                        menuIconImage = Icons.Rounded.Menu,
                        navigationIconOnClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                            }
                        },
                        iconImage = icon,
                        iconOnclick = { annoyed = !annoyed; showBottomSheet.value = true },
                        title = titleName,
                        subTitle = subTitle
                    ) {

                        if (showRegisterDialog) {
                            RegisterDialog(onDismissRequest = { showRegisterDialog = false }) {
                                showRegisterDialog = false
                            }
                        }

                        if (showModifyDialog.value) {
                            ModifyDialog(onDismissRequest = { showModifyDialog.value = false }, user = modifyUser.value) {
                                showModifyDialog.value = false
                            }
                        }

                        if (showUserListDialog) {
                            UserListDialog(
                                onDismissRequest = { showUserListDialog = false; showLoginDialog = false },
                                onConfirmation = {

                                    if (showLoginDialog) {
                                        if (userProfile.id != it.id) {
                                            userProfile = it
                                            scope.launch {
                                                DataStoreUtil.saveData(this@MainActivity, it.userName, it.age, it.gender)
                                            }
                                            showUserListDialog = false
                                            showLoginDialog = false
                                        } else {
                                            showToast(this, getString(R.string.you_are_already_login_with_this_user))
                                        }
                                    } else {
                                        modifyUser.value = it
                                        showUserListDialog = false
                                        showModifyDialog.value = true
                                    }
                                },
                                isClickable = userListClickable
                            )
                        }

                        BottomSheet(showBottomSheet = showBottomSheet)

                        Image(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Home background",
                            modifier = Modifier
                                .fillMaxHeight(0.7f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.FillBounds
                        )

                        Row(Modifier.fillMaxSize()) {
                            Card(
                                shape = RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp),
                                modifier = Modifier
                                    .align(Alignment.Bottom)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                                    .verticalScroll(rememberScrollState()),
                                colors = CardDefaults.cardColors(containerColor = onSecondary_alt)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    color = OnPrimary_alt,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 30.dp),
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = stringResource(id = R.string.introduction),
                                    color = OnPrimary_alt,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 10.dp, start = 50.dp, end = 50.dp),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )

                                Button(
                                    onClick = { btnUserGuideOnClick() }, modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(20.dp)
                                        .width(270.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Info,
                                        contentDescription = "Info Icon",
                                        Modifier.padding(end = 10.dp)
                                    )
                                    Text(text = getString(R.string.how_it_works), fontSize = 16.sp)
                                }

                                Button(
                                    onClick = {btnTestActivityOnClick()}, modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                                        .width(270.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Create,
                                        contentDescription = "Edit Icon",
                                        Modifier.padding(end = 10.dp)
                                    )
                                    Text(text = if (testStarted) getString(R.string.continue_test) else getString(
                                        R.string.start_test
                                    ), fontSize = 16.sp)
                                }

                                Button(
                                    onClick = { btnPreviousOnClick() }, modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                                        .width(270.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = getString(R.string.previous_icon),
                                        Modifier.padding(end = 10.dp)
                                    )
                                    Text(text = getString(R.string.previous_results), fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun btnUserGuideOnClick() {
        val intent = Intent(this, UserGuideActivity::class.java)
        startActivity(intent)
    }

    private fun btnPreviousOnClick () {
        if (userProfile.userExists()) {
            val intent = Intent(this, PreviousResultsActivity::class.java)
            startActivity(intent)
        }else {
            showToast(this, getString(R.string.not_login_message))
        }
    }


    private fun btnTestActivityOnClick() {
        if (userProfile.userExists()) {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        } else {
            showToast(this, getString(R.string.not_login_message))
        }
    }

    @Composable
    private fun NavigationDrawerItem(
        text: String,
        iconImage: ImageVector,
        contentDescriptor: String = "",
        onClick: () -> Unit
    ) {
        NavigationDrawerItem(
            label = {
                NavDrawerLabel(
                    text = text,
                    iconImage = iconImage,
                    contentDescriptor = contentDescriptor
                )
            },
            selected = true,
            onClick = onClick,
            modifier = Modifier.padding(30.dp, 10.dp, 30.dp, 0.dp)
        )
    }

    @Composable
    private fun NavDrawerLabel(text: String, iconImage: ImageVector, contentDescriptor: String) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = iconImage, contentDescription = contentDescriptor)
            Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BottomSheet(showBottomSheet: MutableState<Boolean>) {

        val sheetState = rememberModalBottomSheetState()

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false
                    annoyed = !annoyed
                },
                sheetState = sheetState,
                containerColor = Purple80
            ) {

                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)) {

                    Text(text = stringResource(R.string.select_icon), modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), textAlign = TextAlign.Center, fontSize = 20.sp)

                    Divider(modifier = Modifier.padding(bottom = 10.dp))

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                        SelectableIcon(iconHappy = R.drawable.female_icon_happy, iconAnnoyed = R.drawable.female_icon_annoyed) {
                            showBottomSheet.value = false
                        }
                        SelectableIcon(iconHappy = R.drawable.male_icon_happy, iconAnnoyed = R.drawable.male_icon_annoyed) {
                            showBottomSheet.value = false
                        }

                        SelectableIcon(iconHappy = R.drawable.female2_icon_happy, iconAnnoyed = R.drawable.female2_icon_annoyed) {
                            showBottomSheet.value = false
                        }
                        SelectableIcon(iconHappy = R.drawable.pou_icon_happy, iconAnnoyed = R.drawable.pou_icon_annoyed) {
                            showBottomSheet.value = false
                        }

                        SelectableIcon(iconHappy = R.drawable.non_binary_icon_happy, iconAnnoyed = R.drawable.non_binary_icon_annoyed) {
                            showBottomSheet.value = false
                        }

                    }

                }
            }
        }

    }

    @Composable
    private fun SelectableIcon(@DrawableRes iconHappy: Int, @DrawableRes iconAnnoyed: Int, onClick: (() -> Unit)? = null) {

        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val userDAO = UserDAO(context)

        IconButton(onClick = {

            val userAux = userProfile.copy(happyIcon = iconHappy, annoyedIcon = iconAnnoyed)

            scope.launch {
                DataStoreUtil.saveData(context, iconHappy)
            }

            userProfile = userAux

            userDAO.update(userAux)

            annoyed = !annoyed

            if (onClick != null) {
                onClick()
            }

        }, modifier = Modifier.size(50.dp)) {
            Image(painter = painterResource(id = iconHappy), contentDescription = "Happy Icon", contentScale = ContentScale.Fit)
        }

    }
}
