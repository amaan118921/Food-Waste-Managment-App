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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NgoHomeAdapter(private val fragment: Fragment, private val model: CommonViewModel): ListAdapter<ModelClasses, NgoHomeAdapter.NgoHomeViewHolder>(DiffCallback) {


    class NgoHomeViewHolder(binding: NgoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.name
        val add = binding.location
        val location = binding.mapLocation

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NgoHomeViewHolder {
        val adapterLayout = NgoItemBinding.inflate(LayoutInflater.from(parent.context))
        return NgoHomeViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NgoHomeViewHolder, position: Int) {
        val item = getItem(position)

        NgoHomeFragment.binding.pb.visibility = View.INVISIBLE
        try {
            BookMarksFragment.binding.pb.visibility = View.INVISIBLE
        }
        catch (e: Exception) {

        }

        holder.add.text = item.address
        holder.title.text = item.title
        holder.location.setOnClickListener {
            try {
                val action1 = NgoHomeFragmentDirections.actionNgoHomeFragmentToMapsFragment(item.lat, item.lon)
                fragment.findNavController().navigate(action1)
            }
            catch (e: java.lang.Exception) {

            }

            try {
                val action2 = BookMarksFragmentDirections.actionBookMarksFragmentToMapsFragment(item.lat, item.lon)
                fragment.findNavController().navigate(action2)
            }
            catch (e:Exception) {

            }

        }

        holder.itemView.setOnClickListener {
            model.checkBookmark(item)
            model.setData(item)
            try {
                fragment.findNavController().navigate(R.id.action_ngoHomeFragment_to_viewRequestNgoFragment)
            }
            catch (e: Exception) {

            }
            try {
                fragment.findNavController().navigate(R.id.action_bookMarksFragment_to_viewRequestNgoFragment)
            }
            catch (e: Exception) {

            }


        }
    }
}
