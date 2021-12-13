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
import com.example.avazassignment.databinding.ListItemFoodNameBinding
import com.example.avazassignment.model.FoodItemModel


/**
 * RecyclerView Adapter for show selected food items names
 */

class FoodItemNamesListAdapter() :
    ListAdapter<String, FoodItemNameViewHolder>(FoodNameDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemNameViewHolder {
        return FoodItemNameViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: FoodItemNameViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.itemName = (position + 1).toString() + ". " + getItem(position)
        }



        holder.bind()


    }


}


/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class FoodItemNameViewHolder private constructor(val viewDataBinding: ListItemFoodNameBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_food_name

        public fun from(parent: ViewGroup): FoodItemNameViewHolder {
            val withDataBinding: ListItemFoodNameBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                LAYOUT,
                parent,
                false
            )
            return FoodItemNameViewHolder(withDataBinding)
        }


    }


    public fun bind(
    ) {

    }

}


class FoodNameDiffCallback :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem;
    }
}