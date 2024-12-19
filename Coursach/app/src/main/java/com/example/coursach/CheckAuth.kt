package com.example.coursach

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.coursach.data.model.UserState


data class AuthResult(
    val message: MutableState<String>,
    val isSuccess: MutableState<Boolean>
)

@Composable
fun CheckAuthStatus(userState: UserState, currentUserState: MutableState<String>) : AuthResult  {
    val authLog = remember { mutableStateOf("") }
    val authSuc = remember { mutableStateOf(false)}
    when (userState) {
        is UserState.Loading -> {
            LoadingComponent()
            authSuc.value = false
        }
        is UserState.Success -> {
            currentUserState.value = userState.message
            authLog.value = userState.message
            authSuc.value = true
        }
        is UserState.SuccessLogOut -> {
            currentUserState.value = userState.message
            authLog.value = userState.message
            authSuc.value = false
        }
        is UserState.Error -> {
            currentUserState.value = userState.message
            authLog.value = userState.message
            authSuc.value = false
        }

    }
    return AuthResult(authLog, authSuc)
}
