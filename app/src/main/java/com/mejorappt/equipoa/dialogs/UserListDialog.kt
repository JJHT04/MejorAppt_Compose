package com.mejorappt.equipoa.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mejorappt.equipoa.R
import com.mejorappt.equipoa.db.UserDAO
import com.mejorappt.equipoa.model.UserProfile
import com.mejorappt.equipoa.ui.theme.OnPrimary_alt
import com.mejorappt.equipoa.ui.theme.onSecondary_alt

@Composable
fun UserListDialog(isClickable: Boolean = false,
                   onDismissRequest: () -> Unit,
                   onConfirmation: (UserProfile) -> Unit) {

    val context = LocalContext.current
    val userDAO = UserDAO(context)
    val users = userDAO.getAll()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
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
                    text = if (isClickable) stringResource(R.string.select_an_user) else stringResource(
                        R.string.user_list_in_this_device
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Divider()

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(220.dp)
                    .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceEvenly) {

                    users.forEach {
                        User(isClickable = isClickable, user = it, onItemClick = onConfirmation)
                    }

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
                        Text(if (isClickable) stringResource(R.string.cancel) else stringResource(R.string.close))
                    }
                }
            }
        }
    }

}

@Composable
private fun User (isClickable: Boolean, user: UserProfile, onItemClick: (UserProfile) -> Unit) {

    var modifier = Modifier
        .fillMaxWidth()
        .padding(6.dp)

    if (isClickable) {
        modifier = modifier.clickable(role = Role.Button) {
            onItemClick(user)
        }
    }

    Card (modifier = modifier, colors = CardDefaults.cardColors(containerColor = onSecondary_alt), shape = RoundedCornerShape(16.dp)) {
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Image(painter = painterResource(id = user.happyIcon), contentDescription = "${user.userName} icon", modifier = Modifier.size(50.dp))
            Text(text = "${user.userName}, ${user.age} ${stringResource(id = R.string.years_old)}, ${user.gender.toString(
                LocalContext.current)}", modifier = Modifier.padding(start = 10.dp), color = OnPrimary_alt)
        }
    }

}