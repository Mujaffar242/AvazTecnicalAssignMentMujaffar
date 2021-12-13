package com.example.avazassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avazassignment.R
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.model.SearchIconModel
import com.example.avazassignment.repository.FoodItemsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFoodItemViewModel @Inject constructor(val repository: FoodItemsRepository) : ViewModel() {

    //live data for hold default food item list
    private val _foodItemList = repository.foodItemList
    val foodItemList: LiveData<MutableList<SearchIconModel>>
        get() = _foodItemList


    //for show loading spinner
    var showLoadingProgressBar=MutableLiveData<Boolean>()




    /*
   * funcation for get food items from api
   * */
    fun getFoodItemByName(searchText:String)
    {
        viewModelScope.launch {
            repository.getFoodItemsFromServer(searchText)
        }
    }



    /*
   * for show loading spinner
   * */
    fun showLoadingSpinner()
    {
        showLoadingProgressBar.value=true;
    }


    /*
   * for hide loading spinner
   * */
    fun hideLoadingSpinner()
    {
        showLoadingProgressBar.value=false;
    }



    /*
    function fpr
    *clear food items value
    * */
    fun clearValues()
    {
        _foodItemList.value=null
    }

}