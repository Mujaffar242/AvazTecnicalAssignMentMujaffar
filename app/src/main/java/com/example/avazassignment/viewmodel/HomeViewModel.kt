package com.example.avazassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avazassignment.R
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.repository.FoodItemsRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(val repository: FoodItemsRepository) : ViewModel() {

    //live data for hold food items name
     var foodItemsNameList = MutableLiveData<MutableList<String>>()


}