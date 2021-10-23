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
import com.example.foodwastemanagmentapp.databinding.FragmentBookMarksBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class BookMarksFragment : Fragment() {

    @SuppressLint("StaticFieldLeak")
    companion object {
        @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentBookMarksBinding
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private val live =  MutableLiveData<MutableList<ModelClasses>>()
    private lateinit var adapter: NgoHomeAdapter

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
        binding = FragmentBookMarksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        live.value = mutableListOf()
        adapter = NgoHomeAdapter(requireParentFragment(), model)
        binding.ngoRecyclerView.adapter = adapter

        ref = database.reference.child("bookmarks").child(auth.uid!!)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               for(dataSnapshot: DataSnapshot in snapshot.children) {
                   val item = dataSnapshot.getValue(ModelClasses::class.java)
                   live.value?.add(item!!)
               }
                check()
                binding.ngoRecyclerView.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        live.observe(this.viewLifecycleOwner) { item->
            adapter.submitList(item)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.setTitle("Bookmarks")
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun check() {
        if(live.value!!.isEmpty()) {
            binding.noBookmarks.visibility = View.VISIBLE
            binding.pb.visibility = View.INVISIBLE
        }
        else  {
            binding.noBookmarks.visibility = View.INVISIBLE
        }
    }
}