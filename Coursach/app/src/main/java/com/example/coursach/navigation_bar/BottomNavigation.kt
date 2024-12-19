package com.example.coursach.navigation_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coursach.ui.theme.MyButtonColor
import com.example.coursach.ui.theme.MyButtonColor3


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val list = listOf(
        BottomItems.Screen1_shop,
        BottomItems.Screen2_rent,
        BottomItems.Screen3_repair,
        BottomItems.Screen4_account
    )
    NavigationBar(
        containerColor = Color.White
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentItem = backStackEntry?.destination?.route
        list.forEach{ item ->
            NavigationBarItem(
                selected = currentItem == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = "iconNavBarItem")
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MyButtonColor3,
                    selectedTextColor = MyButtonColor3,
                    selectedIndicatorColor = MyButtonColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    disabledIconColor = Color.Black,
                    disabledTextColor = Color.Black
                )
            )
        }
    }

}