package com.example.avazassignment.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.avazassignment.R
import com.example.avazassignment.databinding.ListItemFoodBinding
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.utils.Constants


/**
 * RecyclerView Adapter for food item list recyclerview
 */

class FoodItemListAdapter() :
    ListAdapter<FoodItemModel, FoodItemViewHolder>(FoodDiffCallback()) {

    //function for mark food item select or unselect
    var onItemClick: ((Int) -> Unit)? = null


    //function for show pop up for add own item in list
    var showAddOwnOptionDialog: (() -> Unit)? = null


    //variable to hold list type custom or default
    var type: Constants.ListType? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.foodItemModel = getItem(position)
        }


        holder.viewDataBinding.root.setOnClickListener {

            /*in case of custom list first item is add icon
            * so by click on add icon we just need to show a pop up and return
            * */
            if (position == 0 && type?.equals(Constants.ListType.CUSTOM)!!) {
                showAddOwnOptionDialog?.invoke()
                return@setOnClickListener
            }

            //in case of default list and other items of custom list we just need to perform simple check uncheck operation
            onItemClick?.invoke(position)
        }

        holder.bind()


    }


}


/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class FoodItemViewHolder private constructor(val viewDataBinding: ListItemFoodBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_food

        public fun from(parent: ViewGroup): FoodItemViewHolder {
            val withDataBinding: ListItemFoodBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                LAYOUT,
                parent,
                false
            )
            return FoodItemViewHolder(withDataBinding)
        }


    }


    public fun bind(
    ) {

    }

}


class FoodDiffCallback :
    DiffUtil.ItemCallback<FoodItemModel>() {
    override fun areItemsTheSame(
        oldItem: FoodItemModel,
        newItem: FoodItemModel
    ): Boolean {
        return oldItem.itemName == newItem.itemName
    }

    override fun areContentsTheSame(
        oldItem: FoodItemModel,
        newItem: FoodItemModel
    ): Boolean {
        return oldItem == newItem;
    }
}