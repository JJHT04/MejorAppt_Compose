package com.mejorappt.equipoa.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.db.UserDAO
import com.mejorappt.equipoa.enums.Gender
import com.mejorappt.equipoa.userProfile


val EMPTY_ERROR_MESSAGE = R.string.this_field_can_t_be_empty
val TOO_LONG_ERROR_MESSAGE = R.string.too_many_characters
val INVALID_AGE = R.string.enter_a_valid_age
val USER_ALREADY_EXISTS_MESSAGE = R.string.this_user_already_exists_in_this_device
val USER_ALREADY_LOGIN_MESSAGE = R.string.you_are_already_login_with_this_user

@Composable
fun UserNameTextField(value: String = "", hint: String = "", valid: MutableState<Boolean>, onValueChange: (String) -> Unit) {
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

            if (it.isBlank() || it.length > 30 || userDAO.userAlreadyExists(it) || userProfile.userName == it) {
                valid.value = false
                error = true
            } else {
                valid.value = true
                error = false
                onValueChange(text)
            }

            errorMessage = if (it.isBlank()) context.getString(EMPTY_ERROR_MESSAGE) else if (it.length > 30) context.getString(
                TOO_LONG_ERROR_MESSAGE) else if (userDAO.userAlreadyExists(it) && userProfile.userName != it) context.getString(
                USER_ALREADY_EXISTS_MESSAGE) else if (userProfile.userName == it) context.getString(
                USER_ALREADY_LOGIN_MESSAGE) else ""
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

@Composable
fun AgeTextField(value: String = "", hint: String = "", valid: MutableState<Boolean>, onValueChange: (Int) -> Unit) {
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var text by rememberSaveable { mutableStateOf(value) }
    var error by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    OutlinedTextField(
        value = text,
        label = { Text(text = hint) },
        onValueChange = {
            if (it.isDigitsOnly()) {
                text = it

                if (it.isBlank()) {
                    valid.value = false
                    error = true
                } else if (it.toInt() in 7..114) {
                    valid.value = true
                    error = false
                    onValueChange(text.toInt())
                } else {
                    valid.value = false
                    error = true
                }

                errorMessage = if (it.isBlank()) context.getString(EMPTY_ERROR_MESSAGE) else if (it.toInt() !in 7..114) context.getString(INVALID_AGE) else ""
            }


        },
        modifier = Modifier.padding(5.dp),
        isError = error,
        supportingText = { Text(text = errorMessage) },
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = { Icon(imageVector = Icons.Rounded.DateRange, contentDescription = "Person icon") },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(onValueChange: (String) -> Unit) {
    val context = LocalContext.current
    val genderList = Gender.getLocaleStringRepresentations(context)
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf(genderList[0]) }

    Box(
        modifier = Modifier
            .padding(5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .clickable(enabled = false) { },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_gender_24), contentDescription = "Gender Icon") },
                shape = RoundedCornerShape(10.dp),
                label = { Text(text = stringResource(id = R.string.gender)) },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onValueChange(item)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedText: MutableState<String>, onValueChange: (String) -> Unit) {
    val context = LocalContext.current
    val genderList = Gender.getLocaleStringRepresentations(context)
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .clickable(enabled = false) { },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_gender_24), contentDescription = "Gender Icon") },
                shape = RoundedCornerShape(10.dp),
                label = { Text(text = stringResource(R.string.gender)) },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText.value = item
                            expanded = false
                            onValueChange(item)
                        }
                    )
                }
            }
        }
    }
}