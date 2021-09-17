package com.example.foodwastemanagmentapp.ngo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentNgoHomeBinding
import com.example.foodwastemanagmentapp.restaurants.RestaurantHomeAdapter
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NgoHomeFragment : Fragment() {

  private lateinit var adapter: NgoHomeAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var userList = mutableListOf<ModelClasses>()

    companion object {
         @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentNgoHomeBinding
    }

    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNgoHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            auth = FirebaseAuth.getInstance()
        if(auth.uid == null) {
            findNavController().navigate(R.id.action_ngoHomeFragment_to_hotelRegisterFragment)
        }
            binding.pb.visibility = View.VISIBLE
            database = FirebaseDatabase.getInstance()
            reference = database.reference.child("2021")
        adapter = NgoHomeAdapter(requireParentFragment(), model)

        binding.ngoRecyclerView.adapter = adapter
            reference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for(dataSnapshot: DataSnapshot in snapshot.children) {
                        val data: ModelClasses? = dataSnapshot.getValue(ModelClasses::class.java)
                        userList.add(data!!)
                    }
                    binding.ngoRecyclerView.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        adapter.submitList(userList)
    }
}