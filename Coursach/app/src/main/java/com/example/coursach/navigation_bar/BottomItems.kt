package com.example.coursach.navigation_bar

import com.example.coursach.R


sealed class BottomItems(val title: String, val icon: Int, val route: String) {
    object Screen1_shop: BottomItems("Купить", R.drawable.icon_shop, "Screen_1_shop")
    object Screen2_rent: BottomItems("Арендовать", R.drawable.icon_rent, "Screen_2_rent")
    object Screen3_repair: BottomItems("Ремонт", R.drawable.icon_repair, "Screen_3_repair")
    object Screen4_account: BottomItems("Профиль", R.drawable.icon_account, "Screen_4_account")

}