package com.example.passwordmanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.Utils.EncryptionUtils
import com.example.passwordmanager.database.PasswordEntry
import com.example.passwordmanager.viewmodel.PasswordViewModel
import com.example.passwordmanager.R
import com.example.passwordmanager.Utils.Utils
import com.example.passwordmanager.Utils.Utils.PasswordStrength


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreen(viewModel: PasswordViewModel) {

    val sheetState = rememberModalBottomSheetState()
    var showAddPasswordBottomSheet by remember { mutableStateOf(false) }
    var showViewPasswordBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var passwordList = remember { mutableStateListOf<PasswordEntry>() }
    passwordList = viewModel.passwords
    var selectedPasswordEntry: PasswordEntry? = null

    var accountState = remember { mutableStateOf("") }
    var emailState = remember { mutableStateOf("") }
    var passwordState = remember { mutableStateOf("") }

    val addPassword = {
        viewModel.addPassword(accountState.value, emailState.value, passwordState.value)
        accountState.value = ""
        emailState.value = ""
        passwordState.value = ""
        passwordList = viewModel.passwords
        showAddPasswordBottomSheet = false
    }
    val onDismissAddPasswordBottomSheet = {
        showAddPasswordBottomSheet = false
    }
    val deletePassword = {
        selectedPasswordEntry?.let { viewModel.deletePassword(it) }
        passwordList = viewModel.passwords
        showViewPasswordBottomSheet = false
    }
    val onDismissViewPasswordBottomSheet = {
        showViewPasswordBottomSheet = false
    }
    fun passwordItemClick(entry: PasswordEntry): PasswordEntry {
        selectedPasswordEntry = entry
        showViewPasswordBottomSheet = true
        return entry
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPasswordBottomSheet = true },
                containerColor = colorResource(id = R.color.blue),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.password_manager),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 24.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = colorResource(id = R.color.gray_10)
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(passwordList) { password ->
                        PasswordEntryItem(entry = password, onItemClicked = { passwordEntry ->
                            passwordItemClick(passwordEntry)
                        })
                    }
                }

                if (showAddPasswordBottomSheet == true) {
                    AddPasswordBottomSheet(
                        accountState.value,
                        { accountState.value = it },
                        emailState.value,
                        { emailState.value = it },
                        passwordState.value,
                        { passwordState.value = it },
                        sheetState,
                        onDismissAddPasswordBottomSheet,
                        addPassword
                    )

                }

                if (showViewPasswordBottomSheet == true) {
                    selectedPasswordEntry?.let { ViewPasswordBottomSheet(viewModel,it,sheetState,onDismissViewPasswordBottomSheet,deletePassword) }
                }

            }
        },
    )
}


@Composable
fun PasswordEntryItem(entry: PasswordEntry, onItemClicked: (PasswordEntry) -> PasswordEntry) {
    Card(
        modifier = Modifier
            .padding(bottom = 18.dp)
            .clickable {
                onItemClicked(entry)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(50.dp))
                .border(1.dp, colorResource(id = R.color.gray_20), RoundedCornerShape(50.dp))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.serviceName,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(id = R.string.password_star),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.gray),
                modifier = Modifier.padding(top = 5.dp, start = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painterResource(id = R.drawable.ic_aerrow_right),
                contentDescription = "Show Details"
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordBottomSheet(
    account: String,
    onAccountValueChange: (String) -> Unit,
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAddPassword: () -> Unit
) {

    // Validation state variables
    var accountError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Validation functions
    fun validateAccount(): Boolean {
        accountError = if (account.isBlank()) "Account name cannot be empty" else null
        return accountError == null
    }

    fun validateEmail(): Boolean {
        emailError = if (email.isBlank()) {
            "Email or UserName cannot be empty"
        } else {
            null
        }
        return emailError == null
    }

    fun validatePassword(): Boolean {
        passwordError = if (password.isBlank()) "Password cannot be empty" else null
        return passwordError == null
    }

    fun validateAll(): Boolean {
        val isAccountValid = validateAccount()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return isAccountValid && isEmailValid && isPasswordValid
    }

    val passwordStrength = Utils.calculatePasswordStrength(password)



    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Content area
            Column(modifier = Modifier.padding(30.dp)) {
                OutlinedTextField(
                    value = account,
                    onValueChange = {
                        onAccountValueChange(it)
                        if (accountError != null) validateAccount()
                    },
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.account_name), style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(
                                    id = R.color.gray_30
                                )
                            )
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                if (accountError != null) {
                    Text(
                        text = accountError!!,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Spacer(modifier = Modifier.height(22.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        onEmailValueChange(it)
                        if (emailError != null) validateEmail()
                    },
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.user_or_email), style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(
                                    id = R.color.gray_30
                                )
                            )
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Spacer(modifier = Modifier.height(22.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        onPasswordValueChange(it)
                        if (passwordError != null) validatePassword()
                    },
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.password), style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(
                                    id = R.color.gray_30
                                )
                            )
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Password strength meter
                PasswordStrengthMeter(passwordStrength)
            }


            Button(
                onClick = {
                    if (validateAll()) {
                        onAddPassword.invoke()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.black))
            ) {
                Text(
                    text = stringResource(id = R.string.add_new_account),
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 5.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPasswordBottomSheet(
    viewModel: PasswordViewModel,
    passwordEntry: PasswordEntry,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onDelatePassword: () -> Unit
) {

    var passwordVisible by remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {

            // Content area
            Column() {
                Text(
                    text = stringResource(id = R.string.account_detail),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.blue)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(id = R.string.account_type),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_40)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = passwordEntry.serviceName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.black)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(id = R.string.user_or_email),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_40)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = passwordEntry.emailOrName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.black)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(id = R.string.password),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_40),
                )
                Spacer(modifier = Modifier.height(2.dp))


                Row(modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    var isPasswordVisible = remember { mutableStateOf(false) }

                    Text(
                        text = if (!isPasswordVisible.value) stringResource(id = R.string.password_star) else EncryptionUtils.decrypt(passwordEntry.encryptedPassword,
                            EncryptionUtils.convertStringToKey(passwordEntry.key)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.black),
                    )

                    val painter=if (isPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    Icon(imageVector = painter, contentDescription = null, modifier = Modifier.clickable {
                        isPasswordVisible.value = !isPasswordVisible.value
                    })


                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.black))
                ) {
                    Text(
                        text = stringResource(id = R.string.edit),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 5.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDelatePassword.invoke()
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red))
                ) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        color = colorResource(id = R.color.white),
                        modifier = Modifier.padding(vertical = 5.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordStrengthMeter(passwordStrength: PasswordStrength) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(passwordStrength.color, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = passwordStrength.displayText,
                color = passwordStrength.color,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}





