package com.example.coursach

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursach.data.model.RepairCar
import com.example.coursach.data.model.UserShopOrRent
import com.example.coursach.data.model.UserState
import com.example.coursach.navigation_bar.CarModel
import com.example.coursach.network.SupabaseClient
import com.example.coursach.network.SupabaseClient.client
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import java.lang.Boolean.TRUE

class SubabaseAuthViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState


    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            try {
                client.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                _userState.value = UserState.Success("Registred user successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error ${e.message}")
            }
        }
    }

    fun login(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            try {
                client.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                _userState.value = UserState.Success("Logged in successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                client.auth.signOut()
                _userState.value = UserState.SuccessLogOut("SignOut successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun isUserIsLoggedIn(
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val session = client.auth.currentSessionOrNull()
                if (session == null) {
                    _userState.value = UserState.Error("User is not logged in!")
                } else {
                    client.auth.retrieveUserForCurrentSession(updateSession = true)
                    client.auth.refreshCurrentSession()
                    _userState.value = UserState.Success("User is already logged in!")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }


    suspend fun getShop(): List<CarModel> {
        try {
            val data = client.postgrest["CarForSale"]
                .select()
                .decodeList<CarModel>()

//            Log.d("MyLog", "Данные получены: $data")


            return data
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
            throw e
        }
    }

    suspend fun getUsers(): List<UserShopOrRent> {
        try {
            val data = client.postgrest["UserShopOrRent"]
                .select()
                .decodeList<UserShopOrRent>()

//            Log.d("MyLog", "Данные получены: $data")

            if (data.isEmpty()) {
                Log.d("MyLog", "Данные магазина пусты")
            } else {
                Log.d("MyLog", "Получено ${data.size} элементов")
            }

            return data
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
            throw e
        }
    }

    fun updateShop(uuidToUpdate: String, valueToUpdate: Boolean) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                client.postgrest["UserShopOrRent"]
                    .update(
                        {
                            set("isWantShop", valueToUpdate)
                        }
                    ) {
                        filter {

                            eq("uuid", uuidToUpdate)
                        }
                    }
                _userState.value = UserState.Success("Update isWantShop successfully!")

            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    fun updateRent(uuidToUpdate: String, valueToUpdate: Boolean) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                client.postgrest["UserShopOrRent"]
                    .update(
                        {
                            set("isWantRent", valueToUpdate)
                        }
                    ) {
                        filter {

                            eq("uuid", uuidToUpdate)
                        }
                    }
                _userState.value = UserState.Success("Update isWantShop successfully!")

            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
//    fun updateUserShop(uuidToUpdate: String, valueToUpdate: Boolean){
//    viewModelScope.launch {
//            try{
//                _userState.value = UserState.Loading
//                client.postgrest["UserShopOrRent"]
//                    .update(
//                        {
//                            Log.d("Mylog", "$uuidToUpdate")
//                            set("isWantShop", valueToUpdate)
//                        }
//                    ){
//                        filter {
//
//                            eq("uuid", uuidToUpdate)
//                        }
//                    }
//                _userState.value = UserState.Success("Update isWantShop successfully!")
//
//            } catch (e:Exception){
//                _userState.value = UserState.Error("Error: ${e.message}")
//            }
//        }
//    }


    suspend fun getUuidCurrentUser(): String? {
        val infCurrentUser = client.auth.retrieveUserForCurrentSession().identities
        val uuidCurrentUser = infCurrentUser?.first()?.userId
//        Log.d("Mylog", "uuid = $uuidCurrentUser")

        return uuidCurrentUser
    }

    fun buyCar(context: Context, valueToUpdate: Boolean) {
        viewModelScope.launch {
            try {
                val uuid = getUuidCurrentUser()
                if (uuid != null) {
                    updateShop(uuidToUpdate = uuid, valueToUpdate = valueToUpdate)
//                    Log.d("Mylog", "Данные должны были обновиться. uuid = $uuid, ")
                    _userState.value = UserState.Success("Данные обновились успешно!")

                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun rentCar(context: Context, valueToUpdate: Boolean) {
        viewModelScope.launch {
            try {
                val uuid = getUuidCurrentUser()
                if (uuid != null) {
                    updateRent(uuidToUpdate = uuid, valueToUpdate = valueToUpdate)
//                    Log.d("Mylog", "Данные должны были обновиться. uuid = $uuid, ")
                    _userState.value = UserState.Success("Данные обновились успешно!")

                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun insertUserShopOrRent() {

        viewModelScope.launch {
            try {
                val uuid = getUuidCurrentUser()
//                Log.d("MyLog", "Этап перед проверкой на значение айдишника! Id = $uuid")

                if (uuid != null) {
                    _userState.value = UserState.Loading
//                    Log.d("MyLog", "Проверка на не нулевое значение айдишника прошло")
                    val existingUser = client.postgrest["UserShopOrRent"]
                        .select {
                            filter {
                                eq("uuid", uuid)
                            }
                        }
                        .decodeSingleOrNull<UserShopOrRent>()

                    if (existingUser == null) {
//                        Log.d("MyLog", "Проверка на существующего пользователя прошла, пользователь не найден!")
                        client.postgrest["UserShopOrRent"].insert(

                            UserShopOrRent(
                                uuid = uuid,
                                isWantShop = false,
                                isWantRent = false
                            )
                        )
                        _userState.value =
                            UserState.Success("Добавление данных в таблицу прошло успешно!")
                    } else {
                        Log.d(
                            "MyLog",
                            "Проверка на пользователя не прошла. ПОЛЬЗОВАТЕЛЬ СУЩЕСТВУЕТ!"
                        )
                    }


//                    Log.d("MyLog", "Конец проверок на добавление перед catch!")
                }


            } catch (e: Exception) {
                Log.d("MyLog", "Сработал catch Ошибка: $e.message")
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    suspend fun getRent(): List<CarForRentModel> {
        try {
            val data = client.postgrest["CarForRent"]
                .select()
                .decodeList<CarForRentModel>()

            Log.d("MyLog", "Данные Об аренде получены: $data")


            return data
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки данных магазина: ${e.message}")
            throw e
        }
    }

    fun insertrepairCar(typeRepair: String, vin: String) {

        viewModelScope.launch {
            try {

                val uuid = getUuidCurrentUser()
//                Log.d("MyLog", "Этап перед проверкой на значение айдишника! Id = $uuid")

                if (uuid != null && typeRepair != null && vin != null) {
//                    Log.d("MyLog", "Проверка на не нулевое значение айдишника прошло")


                        client.postgrest["RepairCar"].insert(
                            RepairCar(
                                uuid = uuid,
                                typeRepair = typeRepair,
                                vin = vin
                            )
                        )
                    } else {
                        Log.d(
                            "MyLog",
                            "Проверка на добавление данных об обслуживании не прошла!"
                        )
                    }


//                    Log.d("MyLog", "Конец проверок на добавление перед catch!")



            } catch (e: Exception) {
                Log.d("MyLog", "Сработал catch Ошибка: $e.message")
            }
        }
    }


}

suspend fun screenShopById(id: Int) =
    client.from("CarForSale")
        .select(
            columns = Columns.list(
                "id",
                "name",
                "price",
                "engine",
                "horsepower",
                "icon",
                "vin",
                "typeCar"
            )
        ) {
            filter {
                CarModel::id eq id
            }
        }
        .decodeSingle<CarModel>()

suspend fun screenRentById(id: Int) =
    client.from("CarForRent")
        .select(
            columns = Columns.list(
                "id",
                "name",
                "pricePerHour",
                "engine",
                "horsePower",
                "icon1",
                "icon2",
                "icon3",
                "rating"
            )
        ) {
            filter {
                CarForRentModel::id eq id
            }
        }
        .decodeSingle<CarForRentModel>()

//suspend fun imageRentById(id: Int) =
//    client.from("CarForRent")
//        .select(
//            columns = Columns.list(
//                "icon1",
//                "icon2",
//                "icon3",
//            )
//        ) {
//            filter {
//                CarForRentModel::id eq id
//            }
//        }
//        .decodeSingle<CarForRentModel>()
//
//
