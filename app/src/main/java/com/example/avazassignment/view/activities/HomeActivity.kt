package com.example.avazassignment.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.avazassignment.R
import com.example.avazassignment.databinding.ActivityHomepageBinding
import com.example.avazassignment.databinding.ActivityOnboardingBinding
import com.example.avazassignment.view.adapters.FoodItemListAdapter
import com.example.avazassignment.view.adapters.FoodItemNamesListAdapter
import com.example.avazassignment.viewmodel.HomeViewModel
import com.example.avazassignment.viewmodel.OnBoardingViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    //for hold home activity binding
    private lateinit var binding: ActivityHomepageBinding

    //for hold home viewmodel
    @Inject
    lateinit var viewModel: HomeViewModel

    /**
     * RecyclerView Adapter for converting a list of food items data to list
     */
    private var viewModelAdapter: FoodItemNamesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        //inject home viewmodel by dragger
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        //initialize binding
        binding=DataBindingUtil.setContentView(this, R.layout.activity_homepage)

        //initialize adapter
        viewModelAdapter= FoodItemNamesListAdapter()

        //set adapter to recyclerview
        binding.foodItemNameRecyclerview.adapter=viewModelAdapter

        //get list of selected data form previous page and add it to viewmodel selected items list
       viewModel.foodItemsNameList.value= intent.getSerializableExtra("list") as ArrayList<String>

        //observe the data selected food list and add it to recylerview
        viewModel.foodItemsNameList.observe(this, Observer {
            viewModelAdapter?.submitList(it)
        })


    }
}