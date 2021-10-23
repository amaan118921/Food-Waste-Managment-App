package com.example.foodwastemanagmentapp.ngo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.ViewRequestNgoBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ViewRequestNgoFragment: Fragment() {
    private lateinit var binding: ViewRequestNgoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var key by Delegates.notNull<Int>()
    private var bookmarkedList = mutableListOf<ModelClasses>()
    private lateinit var ref: DatabaseReference
    private val list = MutableLiveData<MutableList<ModelClasses>>()
    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    private lateinit var item: ModelClasses

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewRequestNgoBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pb.visibility = View.VISIBLE
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        setUpToolbar()



        binding.pb.visibility = View.VISIBLE
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        item = model.getData()
        val ref = database.reference.child("bookmarks").child(auth.uid!!)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap: DataSnapshot in snapshot.children) {
                    val item = snap.getValue(ModelClasses::class.java)
                    bookmarkedList.add(item!!)
                }
                binding.pb.visibility = View.INVISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        checkBookmark()

        list.value = mutableListOf()
        binding.titleName.text = item.title
        binding.addressAdd.text = item.address
        binding.date.text = "10 Sep"
        binding.desc.text = item.desc
        binding.contact.setText(item.contact)
        binding.status.setText(item.status)

    }


//    private fun addBookmark() {
//        binding.pb.visibility = View.VISIBLE
//        ref = database.reference.child("bookmarks").child(auth.uid!!)
//        val updatedItem = item
//        ref.push().setValue(updatedItem).addOnCompleteListener(object: OnCompleteListener<Void> {
//            @SuppressLint("UseCompatLoadingForDrawables")
//            override fun onComplete(p0: Task<Void>) {
//                if(p0.isSuccessful) {
//                    binding.pb.visibility = View.INVISIBLE
//
//                    Toast.makeText(requireContext(), "Bookmark Added", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })
//    }

    private fun showLocation() {
        val action = ViewRequestNgoFragmentDirections.actionViewRequestNgoFragmentToMapsFragment(item.lat, item.lon)
        Toast.makeText(requireContext(), "lat${item.lat} + lon${item.lon}", Toast.LENGTH_SHORT).show()
        findNavController().navigate(action)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkBookmark() {
        if(model.getBookmark()==1) {
            binding.toolbar.menu.getItem(1).setIcon(resources.getDrawable(R.drawable.ic_baseline_bookmark_24))
        }
        else {
            binding.toolbar.menu.getItem(1).setIcon(resources.getDrawable(R.drawable.ic_baseline_bookmark_border_24))
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.inflateMenu(R.menu.view_ngo_bar)
        binding.toolbar.title = "Request details"
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener(object : androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.bookmarks ->  {
                        checkBookmark(this@ViewRequestNgoFragment.item)
                    }
                    R.id.location -> {
                        showLocation()
                    }
                }
                return true
            }
        })
    }

    private fun checkBookmark(item: ModelClasses) {
        if(bookmarkedList.contains(item)) {
            model.bookmarkedList.remove(item)
            bookmarkedList.remove(item)
            removeItem(item)
        }
        else  {
            addItem(item)
        }
    }


    private fun addItem(item: ModelClasses) {


        binding.pb.visibility = View.VISIBLE
        ref = database.reference.child("bookmarks").child(auth.uid!!)
        ref.push().setValue(item).addOnCompleteListener(object: OnCompleteListener<Void> {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onComplete(p0: Task<Void>) {
                if(p0.isSuccessful) {
                    binding.pb.visibility = View.INVISIBLE
                    binding.toolbar.menu.getItem(1).setIcon(resources.getDrawable(R.drawable.ic_baseline_bookmark_24))
                    Toast.makeText(requireContext(), "Bookmark Added", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun removeItem(item: ModelClasses) {



        val dataRef = database.reference.child("bookmarks").child(auth.uid!!)
        val query =  dataRef.orderByChild("title").equalTo(item.title)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap: DataSnapshot in snapshot.children) {
                    snap.ref.removeValue().addOnCompleteListener(object: OnCompleteListener<Void> {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onComplete(p0: Task<Void>) {
                            binding.pb.visibility = View.INVISIBLE
                            binding.toolbar.menu.getItem(1).setIcon(resources.getDrawable(R.drawable.ic_baseline_bookmark_border_24))
                            Toast.makeText(requireContext(), "Bookmark Removed", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}



