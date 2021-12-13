package com.example.avazassignment.view.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.avazassignment.R
import com.example.avazassignment.databinding.ActivityOnboardingBinding
import com.example.avazassignment.databinding.AddNewItemDialogBinding
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.model.SearchIconModel
import com.example.avazassignment.utils.Constants
import com.example.avazassignment.view.adapters.FoodItemListAdapter
import com.example.avazassignment.view.adapters.FoodItemSearchListAdapter
import com.example.avazassignment.viewmodel.OnBoardingViewModel
import com.example.avazassignment.viewmodel.SearchFoodItemViewModel
import dagger.android.AndroidInjection
import java.util.ArrayList
import javax.inject.Inject

class OnBoardingActivity : AppCompatActivity() {


    private lateinit var binding: ActivityOnboardingBinding

    //for hold onboarding viewmodel
    @Inject
    lateinit var viewModel: OnBoardingViewModel

    @Inject
    lateinit var serachFoodItemViewModel: SearchFoodItemViewModel

    /**
     * RecyclerView Adapter for default food items
     */
    private var defaultFoodItemListAdapter: FoodItemListAdapter? = null


    //variable for hold progress dialog
    lateinit var progressDialog: ProgressDialog


    /**
     * RecyclerView Adapter for custom food items
     */
    private var customFoodItemListAdapter: FoodItemListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        //inject viewmodel by dragger
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        //init progress dialog
        progressDialog = ProgressDialog(this)

        //hide action bar
        supportActionBar?.hide()

        //init data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        binding.onBoardingViewModel = viewModel

        //create and set adapter for default food items
        defaultFoodItemListAdapter = FoodItemListAdapter()
        defaultFoodItemListAdapter?.type = Constants.ListType.DEFAULT
        binding.foodItemRecyclerview.adapter = defaultFoodItemListAdapter


        //create and set adapter for custom food items
        customFoodItemListAdapter = FoodItemListAdapter()
        customFoodItemListAdapter?.type = Constants.ListType.CUSTOM
        binding.myFoodItemRecyclerview.adapter = customFoodItemListAdapter


        //observer default food item list
        viewModel.foodItemList.observe(this, Observer {
            defaultFoodItemListAdapter?.submitList(it)
        })


        //observer custom food item list
        viewModel.customFoodItemList.observe(this, Observer {
            customFoodItemListAdapter?.submitList(it)
        })

        //show show dialog crate own list
        customFoodItemListAdapter?.showAddOwnOptionDialog={
            addNewItemDialog()
        }

        //default food item list item click handler
        defaultFoodItemListAdapter?.onItemClick = {
            viewModel.onItemClick(it,Constants.ListType.DEFAULT)
        }


        //custom food item list item click handler
        customFoodItemListAdapter?.onItemClick = {
            viewModel.onItemClick(it,Constants.ListType.CUSTOM)
        }


        /*
        * for observe data change in case of default list
        * */
        viewModel.notifyDataChangeDefaultList.observe(this, Observer {
            defaultFoodItemListAdapter?.notifyItemChanged(it)
        })


        /*
       * for observe data change in case of custom list
       * */
        viewModel.notifyDataChangeCustomList.observe(this, Observer {
            customFoodItemListAdapter?.notifyItemChanged(it)
        })

        /*
       * for observe item inserted in custom list
       * */
        viewModel.notifyItemInserInCustomList.observe(this, Observer {
            customFoodItemListAdapter?.notifyItemInserted(it)
        })

        /*
        * for show validation error in toast
        * in case of less then 3 or more then 5 items selection
        * */
        viewModel.validationError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })



        /*
        * for redirect user to home page in case all validation passed
        * */
        viewModel.redirectToHome.observe(this, Observer {
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)

                //get selected items list and put it on intent for send homepage
                val arrayList = viewModel.getSelectedItemList().map { it.itemName }
                intent.putExtra("list", arrayList as ArrayList<String>)

                startActivity(intent)
            }
        })

        /*
        * live data for show and hide loading spinner
        * */
        serachFoodItemViewModel.showLoadingProgressBar.observe(this, Observer {
            if(it)
                progressDialog.show()
            else
                progressDialog.hide()
        })
    }





    /*
    * alert dialog for create own food item list
    * */
    fun addNewItemDialog() {

        //clear old search values each time at the time of dialog open
        serachFoodItemViewModel.clearValues()

        //for hold data binding for diallog
        lateinit var addNewItemDialogBinding: AddNewItemDialogBinding

        //for hold selected item position
        var selectedPosition=-1

        //init data binding
        addNewItemDialogBinding = AddNewItemDialogBinding.inflate(layoutInflater)


        //crate and add adapter for searched items
        var searchItemListAdapter = FoodItemSearchListAdapter()
        addNewItemDialogBinding.foodItemSearchRecyclerview.adapter = searchItemListAdapter



        //create and show alert dialog
        val addNewItemAlertDialogBuilder = AlertDialog.Builder(this)
        addNewItemAlertDialogBuilder.setView(addNewItemDialogBinding.root)
        addNewItemAlertDialogBuilder.setCancelable(true)
        val addNewItemAlertDialog = addNewItemAlertDialogBuilder.create()
        addNewItemAlertDialog.show()


        //add text watcher for call api each time text change
        addNewItemDialogBinding.etSearchFood.addTextChangedListener(textWatcher)



        /*
        * handle save button click
        * */
        addNewItemDialogBinding.saveButton.setOnClickListener {

            //in case of no search text show validation error please enter some text
            if (addNewItemDialogBinding.etSearchFood.text.toString().isNullOrEmpty())
            {
                Toast.makeText(this,"Please enter food item name",Toast.LENGTH_LONG).show()
            }
            //in case user not select any item form searched list
            else if (selectedPosition<0)
            {
                Toast.makeText(this,"Please select at least one item",Toast.LENGTH_LONG).show()
            }
            else{
                /*
                * crate food item model to add in custom list
                * for food item name we can use the name that user enter on search box
                * and for item image we can take selected image url
                * */
                val itemName=addNewItemDialogBinding.etSearchFood.text.toString()
                val itemImage=serachFoodItemViewModel.foodItemList.value?.get(selectedPosition)?.preview_url

                val foodItem=FoodItemModel(itemName,itemImage!!)

                viewModel.addItemToCustomSelectedList(foodItem)

                //dismiss the dialog
                addNewItemAlertDialog.dismiss()
            }


        }


        /*
        * observe the search item
        * and add items to search items adapter
        * */
        serachFoodItemViewModel.foodItemList.observe(this, Observer {

            //add list to adapter
            searchItemListAdapter.submitList(it)

            //dismiss loading spinner
            serachFoodItemViewModel.hideLoadingSpinner()

            //in case of no data hide recyclerview and show no data text
            if(it.isNullOrEmpty())
            {
                addNewItemDialogBinding.noDataTv.visibility=View.VISIBLE
                addNewItemDialogBinding.foodItemSearchRecyclerview.visibility=View.GONE

            }
            //in case of some data show recyclerview and hide no data text
            else
            {
                addNewItemDialogBinding.noDataTv.visibility=View.GONE
                addNewItemDialogBinding.foodItemSearchRecyclerview.visibility=View.VISIBLE
            }

        })

        /*
        * handle search item click
        * */
        searchItemListAdapter.onItemClick={
            searchItemListAdapter.selectedPostion=it
            searchItemListAdapter.notifyItemChanged(selectedPosition)
            searchItemListAdapter.notifyItemChanged(it)
            selectedPosition=it

        }

    }

    /*
    * text watcher for deduct text change
    * */
    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            //  getFoodItemsFromServer(s.toString())
            if(s.length==1)
                serachFoodItemViewModel.clearValues()
            else
            {
                //show loading spinner and call api for get food item icon
                serachFoodItemViewModel.showLoadingSpinner()
                serachFoodItemViewModel.getFoodItemByName(s.toString())

            }
        }
    }







}