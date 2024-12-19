package com.example.coursach

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursach.data.model.UserState
import com.example.coursach.navigation_bar.CarModel
import com.example.coursach.navigation_bar.MainScreen
import com.example.coursach.ui.theme.CoursachTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoursachTheme {
//                MainScreen()
                var isAuthenticated by remember { mutableStateOf(false) }

                if (isAuthenticated) {

                    MainScreen()

                } else {
                    MainScreenLesson(
                        onAuthSuccess = {
                            isAuthenticated = true
                        }
                    )
                }

            }
        }
    }

}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun loadShop(
    viewModel: SubabaseAuthViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        try {
            val shopData = viewModel.getShop()
//            Log.d("MyLog", "Данные магазина: $shopData")
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
        }
    }


}


@Composable
fun MainScreenLesson(
    viewModel: SubabaseAuthViewModel = viewModel(),
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    val userState by viewModel.userState

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var authSuccess by remember { mutableStateOf(false) }
    var currentUserState by remember { mutableStateOf("") }

    if (authSuccess) {
        onAuthSuccess()
        return
    }

    LaunchedEffect(Unit) {
        viewModel.isUserIsLoggedIn(
            context,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = userEmail,
            placeholder = {
                Text(text = "Enter email")
            },
            onValueChange = {
                userEmail = it
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = userPassword,
            placeholder = {
                Text(text = "Enter password")
            },
            onValueChange = {
                userPassword = it
            },
            visualTransformation = PasswordVisualTransformation(mask = '*')
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            viewModel.signUp(
                context,
                userEmail,
                userPassword,
            )
        }) {
            Text(text = "Sign up")
        }

        Button(onClick = {
            viewModel.login(
                context,
                userEmail,
                userPassword,
            )
        }) {
            Text(text = "Log in")
        }

        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                viewModel.logout()

            }) {
            Text(text = "Log out")
        }

//        when (userState) {
//            is UserState.Loading -> {
//                LoadingComponent()
//            }
//            is UserState.Success ->{
//                val message = (userState as UserState.Success).message
//                currentUserState = message
//            }
//            is UserState.Error -> {
//                val message = (userState as UserState.Error).message
//                currentUserState = message
//            }
//
//        }

        val authResult = CheckAuthStatus(userState, remember { mutableStateOf("") })

        if (authResult.isSuccess.value) {


            LaunchedEffect(Unit) {
                viewModel.insertUserShopOrRent()
//                Log.d("MyLog", "Вызов функции на добавление произошел")

            }
            // Переход на другой экран

            authSuccess = true
            Text(text = "Авторизация успешна! Переход на главный экран.")
        } else if (authResult.message.value.isNotEmpty()) {
            Text(text = authResult.message.value)
            authSuccess = false
        }
    }
}









