package com.example.coursach.navigation_bar

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.coursach.CarForRentModel
import com.example.coursach.R
import com.example.coursach.SubabaseAuthViewModel
import com.example.coursach.data.model.UserShopOrRent
import com.example.coursach.loadShop
import com.example.coursach.network.SupabaseClient.client
import com.example.coursach.screenRentById
import com.example.coursach.screenShopById
import com.example.coursach.ui.theme.MyButtonColor
import com.example.coursach.ui.theme.MyButtonColor2
import com.example.coursach.ui.theme.MyButtonColor3
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun Screen1(
    navController: NavHostController,
    viewModel: SubabaseAuthViewModel = viewModel()
) {
    val shopData = remember { mutableStateOf<List<CarModel>>(emptyList()) }
    var uuidCurrentUser: String
    LaunchedEffect(Unit) {
        uuidCurrentUser = viewModel.getUuidCurrentUser().toString()
//        Log.d("Mylog", "ы додик $uuidCurrentUser")
    }




    LaunchedEffect(Unit) {
        try {
            shopData.value = viewModel.getShop()
//            Log.d("MyLog", "Данные магазина загружены: ${shopData.value}")
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxSize()
    ) {
        Text(
            text = "Легковые",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
        )

        // LazyColumn с карточками для каждого автомобиля
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            itemsIndexed(shopData.value) { index, item ->
                // Карточка для каждого элемента
                CarCard(
                    carModel = item,
                    onClick = { navController.navigate("OpenCarScreen/${item.id}") })
            }
        }

        Text(
            text = "Грузовые",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            itemsIndexed(shopData.value) { index, item ->
                // Карточка для каждого элемента
                TruckCard(
                    truckModel = item,
                    onClick = { navController.navigate("OpenCarScreen/${item.id}") })
            }
        }
    }
}


@Composable
fun CarCard(carModel: CarModel, onClick: () -> Unit) { // <-- Добавлено
    if (carModel.typeCar == "Легковая") {
        Card(
            modifier = Modifier
                .padding(end = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onClick() },  // <-- Добавлено
            elevation = CardDefaults.elevatedCardElevation(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = carModel.icon,
                    contentDescription = "Car Image",
                    modifier = Modifier.size(240.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = carModel.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(text = "Цена: ${carModel.price}₽", fontSize = 16.sp)
                Text(
                    text = "Объем двигателя: ${carModel.engine}"
                )
                Text(
                    text = "Л.с: ${carModel.horsepower}"
                )
            }
        }
    }
}

@Composable
fun TruckCard(truckModel: CarModel, onClick: () -> Unit) {
    if (truckModel.typeCar == "Грузовая") {
        Card(
            modifier = Modifier
                .padding(end = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onClick() },
            elevation = CardDefaults.elevatedCardElevation(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = truckModel.icon,
                    contentDescription = "Car Image",
                    modifier = Modifier
                        .size(240.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = truckModel.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Цена: ${truckModel.price}₽",
                    fontSize = 16.sp
                )
                Text(
                    text = "Объем двигателя: ${truckModel.engine}"
                )
                Text(
                    text = "Л.с: ${truckModel.horsepower}"
                )
            }
        }
    }

}


@Composable
fun Screen2(
    navController: NavHostController,
    viewModel: SubabaseAuthViewModel = viewModel()
) {
    val shopData = remember { mutableStateOf<List<CarForRentModel>>(emptyList()) }
    var uuidCurrentUser: String
    LaunchedEffect(Unit) {
        uuidCurrentUser = viewModel.getUuidCurrentUser().toString()
    }

    LaunchedEffect(Unit) {
        try {
            shopData.value = viewModel.getRent()
            Log.d("MyLog", "Данные магазина загружены: ${shopData.value}")
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Есть в наличии",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        Log.d("Mylog", "shopData.value: ${shopData.value}")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(shopData.value) { index, item ->
                CarForRentCard(
                    carForRent = item,
                    onClick = { navController.navigate("OpenCarRentScreen/${item.id}") })
            }
            item {
                Spacer(modifier = Modifier.height(200.dp)) // Отступ внизу, чтобы убедиться, что последний элемент не перекрывается
            }
        }



    }
}

@Composable
fun CarForRentCard(carForRent: CarForRentModel, onClick: () -> Unit) {
    Log.d("Mylog", "Данные получены, тачка: ${carForRent.name}, ${carForRent.id}")
    Card(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,

            ) {
            AsyncImage(
                model = carForRent.icon1,
                contentDescription = "Car Image",
                modifier = Modifier
                    .size(400.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Log.d("MylogCarForRent", "Перед выводом названия машины: ${carForRent.name}")
            Text(
                text = carForRent.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Log.d("MylogCarForRent", "Перед выводом цены за час машины: ${carForRent.pricePerHour}")
            Text(text = "Цена: ${carForRent.pricePerHour}₽", fontSize = 16.sp)
            Text(
                text = "Объем двигателя: ${carForRent.engine}"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Л.с: ${carForRent.horsePower}"
                )
                Row {
                    Text(
                        text = "Оценка: ${carForRent.rating}",
                        fontSize = 18.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.icon_star),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)

                    )
                }


            }

        }
    }

}


@Composable
fun Screen3(
    navController: NavHostController,
    viewModel: SubabaseAuthViewModel = viewModel()
) {
    val buttonIsPressed = remember { mutableStateOf(false) }
    val textFieldvalue = remember { mutableStateOf("") }
    val typeRepair = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxSize(),

    ) {
        Text(
            text = "Обслуживание автомобиля",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 10.dp)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        )


        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Выберите услугу",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)

        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                buttonIsPressed.value = true
                typeRepair.value = "Подсчет расхода топлива"
            },
            colors = ButtonDefaults.buttonColors(containerColor = MyButtonColor, contentColor = Color.Black),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Подсчет расхода топлива"
            )
        }
        Button(
            onClick = {
                buttonIsPressed.value = true
                typeRepair.value = "Консультация со специалистом"
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Консультация со специалистом"
            )
        }
        Button(
            onClick = {
                buttonIsPressed.value = true
                typeRepair.value = "Тех. обслуживание"
            },
            colors = ButtonDefaults.buttonColors(containerColor = MyButtonColor2, contentColor = Color.Black),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Тех. обслуживание"
            )
        }
        Button(
            onClick = {
                buttonIsPressed.value = true
                typeRepair.value = "Поиск запчастей"
            },
            colors = ButtonDefaults.buttonColors(containerColor = MyButtonColor3, contentColor = Color.Black),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Поиск запчастей"
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Column {
            Text(
                text = "Введите VIN номер вашего автомобиля",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp),
            )
            Row {
                TextField(
                    value = textFieldvalue.value,
                    onValueChange = {
                        textFieldvalue.value = it
                    },
                    placeholder = {
                        Text(text = "Enter VIN")
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(200.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        if(textFieldvalue.value != ""){

                            Log.d("Mylog", "textFieldValue: ${textFieldvalue.value}")
                            if(typeRepair.value != ""){
                                viewModel.insertrepairCar(typeRepair = typeRepair.value, vin = textFieldvalue.value)
                            }else{
                                Toast.makeText(context, "Вы не выбрали тип услуги!", Toast.LENGTH_SHORT).show()
                            }

                        }else{
                            Toast.makeText(context, "Вин номер пустой, введите vin номер", Toast.LENGTH_SHORT).show()
                        }

                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_send),
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }
            }

        }




    }
}


@Composable
fun Screen4(
    viewModel: SubabaseAuthViewModel = viewModel()
) {
    val infCurrentUser = remember { mutableStateOf("") }
    val infCurrentUuid = remember { mutableStateOf("") }
    val infCurrentCreated = remember { mutableStateOf("") }
    val infCurrentLastLogin = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxSize(),

        ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Аккаунт",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 20.dp)
                .wrapContentWidth(align = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Почта",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        LaunchedEffect(Unit) {
            infCurrentUser.value = client.auth.retrieveUserForCurrentSession().email.toString()


        }
        LaunchedEffect(Unit) {
            infCurrentUuid.value = client.auth.retrieveUserForCurrentSession().identities?.first()?.userId.toString()
        }
        LaunchedEffect(Unit) {
            infCurrentCreated.value = client.auth.retrieveUserForCurrentSession().createdAt.toString()
        }
        LaunchedEffect(Unit) {
            infCurrentLastLogin.value = client.auth.retrieveUserForCurrentSession().lastSignInAt.toString()
        }
        Text(
            text = infCurrentUser.value,
            modifier = Modifier
                .padding(start = 20.dp),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Уникальный ID",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        Text(
            text = infCurrentUuid.value,
            modifier = Modifier
                .padding(start = 20.dp),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Время создания",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        Text(
            text = infCurrentCreated.value,
            modifier = Modifier
                .padding(start = 20.dp),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Последний вход",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        Text(
            text = infCurrentLastLogin.value,
            modifier = Modifier
                .padding(start = 20.dp),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )
//        Spacer(modifier = Modifier.height(200.dp))
//        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.Black),
//            onClick = {
//                viewModel.logout()
//
//            },
//            modifier = Modifier
//                .padding(horizontal = 50.dp)
//                .fillMaxWidth()
//        ) {
//            Text(text = "Log out")
//        }







    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OpenCarShopScreen(
    carId: Int,
    viewModel: SubabaseAuthViewModel = viewModel(),
) {
    var currentuser: String = ""

    var buttonShopState = remember { mutableStateOf(true) }
    val listUsers = remember { mutableStateOf<List<UserShopOrRent>>(emptyList()) }
    LaunchedEffect(Unit) {
        listUsers.value = viewModel.getUsers()
        currentuser = viewModel.getUuidCurrentUser().toString()
        listUsers.value.forEach() {
            if (it.uuid == currentuser) {
                buttonShopState.value = it.isWantShop
            }
        }
    }


    val context = LocalContext.current
    var carModel by remember {
        mutableStateOf<CarModel?>(null)
    }

    LaunchedEffect(key1 = true) {
        carModel = screenShopById(id = carId)

    }
    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = carModel?.icon,
                contentDescription = "opencarimage",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = carModel?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            )
            Text(
                text = "${carModel?.price}₽",
                fontSize = 24.sp,
            )
            Text(
                text = "${carModel?.engine} л.",
                fontSize = 24.sp,
            )
            Text(
                text = "${carModel?.horsepower} л.с",
                fontSize = 24.sp,
            )
            Text(
                text = "${carModel?.vin}",
                fontSize = 24.sp,
            )
//            LaunchedEffect(key1 = true) {
//                carModel = screenShopById(id = carId)
//
//
//            }

            if (buttonShopState.value) {
                Button(
                    onClick = {
                        buttonShopState.value = false
//                            Log.d("Mylog", "$currentuser\n ${currentuser.toString()}")
                        viewModel.buyCar(context = context, buttonShopState.value)

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                    ),


                    ) {
                    Text(text = "Отменить", color = Color.Black)
                }
            } else {
                Button(
                    onClick = {
                        buttonShopState.value = true
                        viewModel.buyCar(context = context, valueToUpdate = buttonShopState.value)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyButtonColor,
                    ),


                    ) {
                    Text(text = "Купить", color = Color.Black)
                }


            }


        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OpenCarRentScreen(
    carId: Int,
    viewModel: SubabaseAuthViewModel = viewModel(),
) {
    var currentuser: String = ""

    var buttonRentState = remember { mutableStateOf(true) }
    val listUsers = remember { mutableStateOf<List<UserShopOrRent>>(emptyList()) }
    LaunchedEffect(Unit) {
        listUsers.value = viewModel.getUsers()
        currentuser = viewModel.getUuidCurrentUser().toString()
        listUsers.value.forEach() {
            if (it.uuid == currentuser) {
                buttonRentState.value = it.isWantRent
                Log.d("Mylog", "Кнопка: ${buttonRentState.value}")
            }
        }
    }


    val context = LocalContext.current
    var carModel by remember {
        mutableStateOf<CarForRentModel?>(null)
    }

    LaunchedEffect(key1 = true) {
        carModel = screenRentById(id = carId)

    }
    Column(
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center
        ) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    AsyncImage(
                        model = carModel?.icon1,
                        contentDescription = "opencarimage",
                        modifier = Modifier
                            .size(448.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    AsyncImage(
                        model = carModel?.icon2,
                        contentDescription = "opencarimage",
                        modifier = Modifier
                            .size(448.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    AsyncImage(
                        model = carModel?.icon3,
                        contentDescription = "opencarimage",
                        modifier = Modifier
                            .size(448.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = carModel?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            )
            Text(
                text = "Цена/ч: ${carModel?.pricePerHour}₽",
                fontSize = 24.sp,
            )
            Text(
                text = "${carModel?.engine} л.",
                fontSize = 24.sp,
            )
            // ТУТ ЕЩЕ ОТОБРАЖАЮТСЯ ЭЛЕМЕНТЫ

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${carModel?.horsePower} л.с",
                    fontSize = 24.sp,
                )
                Row(
                ) {
                    Text(
                        text = "Оценка: ${carModel?.rating}",
                        fontSize = 18.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.icon_star),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)

                    )
                }

            }
            // ТУТ УЖЕ НЕ ОТОБРАЖАЮТСЯ ЭЛЕМЕНТЫ

            LaunchedEffect(key1 = true) {
                carModel = screenRentById(id = carId)


            }
            Log.d(
                "Mylog",
                "Значение кнопки перед проверкой на значение кнопки: ${buttonRentState.value}"
            )

            if (buttonRentState.value) {
                Button(
                    onClick = {
                        buttonRentState.value = false
//                            Log.d("Mylog", "$currentuser\n ${currentuser.toString()}")
                        viewModel.rentCar(context = context, buttonRentState.value)

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                    ),
                ) {
                    Text(text = "Отменить", color = Color.Black)
                }
            } else {
                Log.d("Mylog", "Сработал блок else у кнопки")
                Button(

                    onClick = {
                        buttonRentState.value = true
                        viewModel.rentCar(context = context, valueToUpdate = buttonRentState.value)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyButtonColor,
                    ),


                    ) {
                    Log.d("Mylog", "Внутри кнопки else")
                    Text(text = "Арендовать", color = Color.Black)
                }


            }


        }
    }
}
