package com.mejorappt.equipoa.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.db.UserDAO
import com.mejorappt.equipoa.enums.Gender
import com.mejorappt.equipoa.firebase.FirebaseUser
import com.mejorappt.equipoa.model.UserProfile
import com.mejorappt.equipoa.userProfile
import com.mejorappt.equipoa.util.AgeTextField
import com.mejorappt.equipoa.util.DataStoreUtil
import com.mejorappt.equipoa.util.EMPTY_ERROR_MESSAGE
import com.mejorappt.equipoa.util.GenderDropdown
import com.mejorappt.equipoa.util.TOO_LONG_ERROR_MESSAGE
import com.mejorappt.equipoa.util.USER_ALREADY_EXISTS_MESSAGE
import com.mejorappt.equipoa.util.showToast
import kotlinx.coroutines.launch

@Composable
fun ModifyDialog(
    user: UserProfile = UserProfile.EMPTY_USER,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val validUserName = rememberSaveable{ mutableStateOf(true) }
    val validAge = rememberSaveable{ mutableStateOf(true) }

    var userName by rememberSaveable { mutableStateOf(user.userName) }
    var gender by rememberSaveable { mutableStateOf(user.gender) }
    var age by rememberSaveable { mutableIntStateOf(user.age) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${stringResource(id = R.string.modify)} ${user.userName}",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                UserNameTextField(value = user.userName, hint = stringResource(R.string.username), valid = validUserName, onValueChange = {
                    userName = it
                }, userProfile = user)

                AgeTextField(value = user.age.toString(), onValueChange = {
                    age = it
                }, hint = stringResource(R.string.age), valid = validAge)

                val selectedText = rememberSaveable { mutableStateOf(user.gender.toString(context)) }

                GenderDropdown (selectedText = selectedText) {
                    val genderMap = Gender.getGenderMap(context)
                    gender = genderMap[it]?: Gender.NON_BINARY
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick =
                        {
                            if (user.userName != userName || user.age != age || user.gender != gender) {
                                val userDAO = UserDAO(context)

                                val userAux = UserProfile(user.id, userName, age, gender, user.happyIcon, user.annoyedIcon, user.date, 3)

                                userDAO.update(userAux)

                                if (user.id == userProfile.id) {
                                    scope.launch {
                                        DataStoreUtil.saveData(context, userName, age, gender)
                                    }

                                    userProfile = userAux
                                }

                                FirebaseUser(context).update(userAux)

                                onConfirmation()
                            } else {
                                showToast(context, R.string.the_user_still_the_same)
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = validUserName.value && validAge.value
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
private fun UserNameTextField(value: String = "", hint: String = "", valid: MutableState<Boolean>, onValueChange: (String) -> Unit, userProfile: UserProfile) {
    var error by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var text by rememberSaveable { mutableStateOf(value) }
    val context = LocalContext.current
    val userDAO = UserDAO(context)
    OutlinedTextField(
        value = text,
        label = { Text(text = hint) },
        onValueChange = {
            text = it

            if (it.isBlank() || it.length > 30 || (userDAO.userAlreadyExists(it) && userProfile.userName != it)) {
                valid.value = false
                error = true
            } else {
                valid.value = true
                error = false
                onValueChange(text)
            }

            errorMessage = if (it.isBlank()) context.getString(EMPTY_ERROR_MESSAGE) else if (it.length > 30) context.getString(TOO_LONG_ERROR_MESSAGE) else if (userDAO.userAlreadyExists(it) && userProfile.userName != it) context.getString(USER_ALREADY_EXISTS_MESSAGE) else ""
        },
        modifier = Modifier.padding(5.dp),
        isError = error,
        supportingText = { Text(text = errorMessage) },
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = "Person icon") },
        singleLine = true
    )
}