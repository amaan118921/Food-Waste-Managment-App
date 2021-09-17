package com.example.foodwastemanagmentapp.ngo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.databinding.NgoItemBinding
import com.example.foodwastemanagmentapp.restaurants.RestaurantHomeAdapter.Companion.DiffCallback
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel

class NgoHomeAdapter(private val fragment: Fragment, private val model: CommonViewModel): ListAdapter<ModelClasses, NgoHomeAdapter.NgoHomeViewHolder>(DiffCallback) {

    class NgoHomeViewHolder(binding: NgoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.name
        val add = binding.location


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NgoHomeViewHolder {
        val adapterLayout = NgoItemBinding.inflate(LayoutInflater.from(parent.context))
        return NgoHomeViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NgoHomeViewHolder, position: Int) {
       val item = getItem(position)
        NgoHomeFragment.binding.pb.visibility = View.INVISIBLE
        holder.add.text = item.address
        holder.title.text = item.title
        holder.itemView.setOnClickListener {
            model.setData(item)
            fragment.findNavController().navigate(R.id.action_ngoHomeFragment_to_viewRequestNgoFragment)
        }
    }
}