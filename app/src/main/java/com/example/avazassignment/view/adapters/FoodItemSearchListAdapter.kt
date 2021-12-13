package com.example.avazassignment.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.avazassignment.R
import com.example.avazassignment.databinding.ListItemFoodBinding
import com.example.avazassignment.databinding.ListItemFoodNameBinding
import com.example.avazassignment.databinding.ListItemSearchFoodBinding
import com.example.avazassignment.model.FoodItemModel
import com.example.avazassignment.model.SearchIconModel


/**
 * RecyclerView Adapter for show searched food item
 */

class FoodItemSearchListAdapter() :
    ListAdapter<SearchIconModel, FoodIteSearchViewHolder>(FoodSearchDiffCallback()) {

    //function for mark food item select or unselect
    var onItemClick: ((Int) -> Unit)? = null

     var selectedPostion:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodIteSearchViewHolder {
        return FoodIteSearchViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: FoodIteSearchViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.searchItemModel = getItem(position)

            if (selectedPostion==position)
                holder.viewDataBinding.checkIcon.visibility=View.VISIBLE
            else
                holder.viewDataBinding.checkIcon.visibility=View.GONE


            holder.viewDataBinding.root.setOnClickListener {
                onItemClick?.invoke(position)
            }



        }



        holder.bind()


    }


}


/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class FoodIteSearchViewHolder private constructor(val viewDataBinding: ListItemSearchFoodBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item_search_food

        public fun from(parent: ViewGroup): FoodIteSearchViewHolder {
            val withDataBinding: ListItemSearchFoodBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                LAYOUT,
                parent,
                false
            )
            return FoodIteSearchViewHolder(withDataBinding)
        }


    }


    public fun bind(
    ) {

    }

}


class FoodSearchDiffCallback :
    DiffUtil.ItemCallback<SearchIconModel>() {
    override fun areItemsTheSame(
        oldItem: SearchIconModel,
        newItem: SearchIconModel
    ): Boolean {
        return oldItem.preview_url == newItem.preview_url
    }

    override fun areContentsTheSame(
        oldItem: SearchIconModel,
        newItem: SearchIconModel
    ): Boolean {
        return oldItem == newItem;
    }
}