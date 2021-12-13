package com.example.avazassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avazassignment.R
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.repository.FoodItemsRepository
import com.example.avazassignment.utils.Constants
import javax.inject.Inject

class OnBoardingViewModel @Inject constructor(val repository: FoodItemsRepository) : ViewModel() {

    //live data for hold default food item list
    private val _foodItemList = MutableLiveData<MutableList<FoodItemModel>>()
    val foodItemList: LiveData<MutableList<FoodItemModel>>
        get() = _foodItemList


    //live data for notify data change in case of default list
    private val _notifyDataChangeDefaultList = MutableLiveData<Int>()
    val notifyDataChangeDefaultList: LiveData<Int>
        get() = _notifyDataChangeDefaultList


    //live data for hold custom food item list
    private val _customFoodItemList = MutableLiveData<MutableList<FoodItemModel>>()
    val customFoodItemList: LiveData<MutableList<FoodItemModel>>
        get() = _customFoodItemList

    //live data for notify data change in case of custom list
    private val _notifyDataChangeCustomList = MutableLiveData<Int>()
    val notifyDataChangeCustomList: LiveData<Int>
        get() = _notifyDataChangeCustomList


    //live data for notify data insert in custom list
    private val _notifyItemInserInCustomList = MutableLiveData<Int>()
    val notifyItemInserInCustomList: LiveData<Int>
        get() = _notifyItemInserInCustomList


    /*live data for hold error message string
    * in case of less then 3 item selection we need to show "please select at least 3 items to continue"
    * if user try to select greater then 5 item selection we need to show "you can select max 5 items"
    * */
    var validationError = MutableLiveData<String>()


    //live data for redirect user  to homepage in case of valid data
    private val _redirectToHome = MutableLiveData<Boolean>()
    val redirectToHome: LiveData<Boolean>
        get() = _redirectToHome


    init {
        //add default food options to list from repository
        _foodItemList.value = repository.getDefaultFoodItemList()
        _customFoodItemList.value = repository.getCustomFoodItem()
    }


    /*
    * function or handle food item click
    * for both cases default list and custom list
    * */
    fun onItemClick(position: Int, listType: Constants.ListType) {

        //handle click in case of default food item list
        if (listType.equals(Constants.ListType.DEFAULT)) {
            if (getSelectedItemList().size == 5 && !_foodItemList.value?.get(position)?.isChecked!!) {
                validationError.value = "you can select max 5 items"
                return
            }
            _foodItemList.value?.get(position)?.isChecked =
                !_foodItemList.value?.get(position)?.isChecked!!
            _notifyDataChangeDefaultList.value = position
        }
        //handle click in case of custom food item list
        else {
            if (getSelectedItemList().size == 5 && !_customFoodItemList.value?.get(position)?.isChecked!!) {
                validationError.value = "you can select max 5 items"
                return
            }
            _customFoodItemList.value?.get(position)?.isChecked =
                !_customFoodItemList.value?.get(position)?.isChecked!!
            _notifyDataChangeCustomList.value = position
        }

    }

    /*
    * get selected items list from both custom list and default list
    * */
    fun getSelectedItemList(): List<FoodItemModel> {


        //get selected item list from default food item list
        val defaultSelectedItems = _foodItemList.value?.filter {
            it.isChecked
        }!!

        //get selected item list from custom list
        val customSelectedItems = _customFoodItemList.value?.filter {
            it.isChecked
        }!!


        //create a new list and add both selected item on that list
        val seletedItemsList = ArrayList<FoodItemModel>()

        if (!defaultSelectedItems.isNullOrEmpty())
            seletedItemsList.addAll(defaultSelectedItems)

        if (!customSelectedItems.isNullOrEmpty())
            seletedItemsList.addAll(customSelectedItems)

        return seletedItemsList

    }


    /*
    * for handle continue button click
    * */
    fun onContinueButtonClick() {
        /*
       check if at least 3 items selected otherwise
        return  and show validation error
        */
        if (getSelectedItemList().size < 3) {
            validationError.value = "please select at least 3 items to continue"
            return
        }

        //in case of valid data
        _redirectToHome.value = true
    }

    /*
    * for reset redirect to home variable
    * */
    fun resetRedirectToHome() {
        _redirectToHome.value = false
    }

    /*
    * function for add values to custom selected item
    * */
    fun addItemToCustomSelectedList(foodItemModel: FoodItemModel) {
        _customFoodItemList.value?.add(foodItemModel)
        _notifyItemInserInCustomList.value = _customFoodItemList.value?.size?.minus(1)
    }
}