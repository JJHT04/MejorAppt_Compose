package com.mejorappt.equipoa.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.mejorappt.equipoa.model.UserProfile
import com.mejorappt.equipoa.userProfile
import com.mejorappt.equipoa.util.AgeTextField
import com.mejorappt.equipoa.util.DataStoreUtil
import com.mejorappt.equipoa.util.GenderDropdown
import com.mejorappt.equipoa.util.UserNameTextField
import kotlinx.coroutines.launch

private var userName by mutableStateOf("")
private var gender by mutableStateOf(Gender.NON_BINARY)
private var age by mutableIntStateOf(0)

@Composable
fun RegisterDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val validUserName = rememberSaveable{ mutableStateOf(false) }
    val validAge = rememberSaveable{ mutableStateOf(false) }

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
                    text = stringResource(id = R.string.register_new_user),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                UserNameTextField(hint = stringResource(R.string.username), valid = validUserName, onValueChange = {
                    userName = it
                })

                AgeTextField(onValueChange = {
                    age = it
                }, hint = stringResource(R.string.age), valid = validAge)

                GenderDropdown {
                    val genderMap = Gender.getGenderMap(context)
                    gender = genderMap[it]?:Gender.NON_BINARY
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
                            val userDAO = UserDAO(context)
                            val maxID = userDAO.getMaxId()

                            scope.launch {
                                DataStoreUtil.saveData(context, maxID, userName, age, gender, getIconHappy(), getIconAnnoyed())
                            }

                            val userprofile = UserProfile(maxID, userName, age, gender, getIconHappy(), getIconAnnoyed())

                            userDAO.insert(userprofile)
                            userProfile = userprofile

                            //TODO FIREBASE -> Insert user = profile.userName, userProfile.age, userProfile.gender.toString()

                            onConfirmation()
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

private fun getIconHappy(): Int {
    return when (gender) {
        Gender.FEMALE -> R.drawable.female_icon_happy
        Gender.MALE -> R.drawable.male_icon_happy
        else -> R.drawable.non_binary_icon_happy
    }
}

private fun getIconAnnoyed(): Int {
    return when (gender) {
        Gender.FEMALE -> R.drawable.female_icon_annoyed
        Gender.MALE -> R.drawable.male_icon_annoyed
        else -> R.drawable.non_binary_icon_annoyed
    }
}