package com.example.foodwastemanagmentapp

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.databinding.FragmentMainRegisterBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterMainFragment: Fragment() {
    private lateinit var binding: FragmentMainRegisterBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var dataRef: DatabaseReference
    private val resList = mutableListOf<ModelClasses>()
    private val ngoList = mutableListOf<ModelClasses>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("ngoUsers")
        dataRef = database.reference.child("users")
        binding.shimmerLayout.startShimmer()
        binding.shimLayout.startShimmer()
        GlobalScope.launch(Dispatchers.IO) {
            dataRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data: DataSnapshot in snapshot.children) {
                        val item = data.getValue(ModelClasses::class.java)
                        resList.add(item!!)
                    }
                    updateResCount(resList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        GlobalScope.launch(Dispatchers.IO) {
            ref.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data:DataSnapshot in snapshot.children) {
                        val item = data.getValue(ModelClasses::class.java)
                        ngoList.add(item!!)
                    }
                    updateNgoCount(ngoList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        binding.b1.setOnClickListener {
            val action = RegisterMainFragmentDirections.actionRegisterMainFragmentToCheckFragment(10)
            findNavController().navigate(action)
        }


        binding.b2.setOnClickListener {
            val action = RegisterMainFragmentDirections.actionRegisterMainFragmentToCheckFragment(20)
            findNavController().navigate(action)
        }
    }

    private fun updateResCount(resList: MutableList<ModelClasses>) {
        binding.n1.setText(resList.size.toString())
    }

    private fun updateNgoCount(ngoList: MutableList<ModelClasses>) {
        binding.n2.setText(ngoList.size.toString())
    }
}