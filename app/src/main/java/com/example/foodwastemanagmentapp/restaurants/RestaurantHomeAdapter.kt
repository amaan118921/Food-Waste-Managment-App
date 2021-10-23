package com.example.foodwastemanagmentapp.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanagmentapp.R

import com.example.foodwastemanagmentapp.databinding.RestaurantItemBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RestaurantHomeAdapter(private val fragment: Fragment, private val model: CommonViewModel): androidx.recyclerview.widget.ListAdapter<ModelClasses,
        RestaurantHomeAdapter.RestaurantHomeViewHolder>(DiffCallback) {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    class RestaurantHomeViewHolder(binding: RestaurantItemBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.name
        val add = binding.location
        val date = binding.date
    }
    companion object {
       val DiffCallback = object : DiffUtil.ItemCallback<ModelClasses>() {
           override fun areItemsTheSame(oldItem: ModelClasses, newItem: ModelClasses): Boolean {
               return oldItem.title == newItem.title
           }

           override fun areContentsTheSame(oldItem: ModelClasses, newItem: ModelClasses): Boolean {
            return oldItem.address == newItem.address
           }



       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHomeViewHolder {
        val adapterLayout = RestaurantItemBinding.inflate(LayoutInflater.from(parent.context))
        return RestaurantHomeViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: RestaurantHomeViewHolder, position: Int) {
        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("2021")
        val data = getItem(position)
        holder.title.text = data.title
        holder.add.text = data.address
        holder.date.text = data.date
        RestaurantHomeFragment.binding.pb.visibility = View.INVISIBLE

        holder.itemView.setOnClickListener {
            model.setData(data)
            fragment.findNavController().navigate(R.id.action_restaurantHomeFragment_to_viewRequestFragment)
        }
    }

}