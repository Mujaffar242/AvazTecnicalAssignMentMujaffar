package com.example.avazassignment.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.avazassignment.R
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.model.SearchIconModel
import com.example.avazassignment.network.GetFoodItemApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

/*
* single data source for application
* we can get data from here in both default list
* and custom list
* */
class FoodItemsRepository @Inject constructor() {

    val foodItemList = MutableLiveData<MutableList<SearchIconModel>>()


    /*
  * function for initialize default list item
  * */
    fun getDefaultFoodItemList(): MutableList<FoodItemModel> {
        val defaultFoodItemList = mutableListOf<FoodItemModel>(
            FoodItemModel("Apple", R.drawable.noun_apple_2422878.toString()),
            FoodItemModel("Burger", R.drawable.noun_burger_1549386.toString()),
            FoodItemModel("Cake", R.drawable.noun_cake_3877809.toString()),
            FoodItemModel("DimSum", R.drawable.noun_dimsum_2835219.toString()),
            FoodItemModel("Dos", R.drawable.noun_dosa_2377601.toString()),
            FoodItemModel("Ice Cream", R.drawable.noun_ice_cream_3879521.toString()),
            FoodItemModel("Pan Cake", R.drawable.noun_pancake_2213498.toString()),
            FoodItemModel("Pizza", R.drawable.noun_pizza_3875906.toString()),
            FoodItemModel("Soup", R.drawable.noun_soup_1130844.toString()),
            FoodItemModel("Sushi", R.drawable.noun_sushi_2220023.toString())
        )

        return defaultFoodItemList
    }


    /*
* function for initialize custom list item
* */
    fun getCustomFoodItem(): MutableList<FoodItemModel> {
        val customFoodItemList = mutableListOf<FoodItemModel>(
            FoodItemModel("New", R.drawable.ic_more_item.toString()),
        )

        return customFoodItemList
    }


    /*
* get items from sever using api
* */
    suspend fun getFoodItemsFromServer(seachText: String) {

        if (seachText.isNullOrEmpty())
            return

        withContext(Dispatchers.IO) {
            try {
                val response = GetFoodItemApi.retrofitService.serachFoodItem(seachText).await()
                foodItemList.postValue(response.icons as MutableList<SearchIconModel>)
            } catch (e: Exception) {
                foodItemList.postValue(null)
            }

        }
    }


}