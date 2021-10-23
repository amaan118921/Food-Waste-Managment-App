package com.example.foodwastemanagmentapp.ngo

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class NgoHomeFragment : Fragment() {

    private lateinit var adapter: NgoHomeAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var userList = mutableListOf<ModelClasses>()
    private lateinit var actionDrawerToggle: ActionBarDrawerToggle

    private var filterList = mutableListOf<ModelClasses>()
    companion object {
         @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentNgoHomeBinding
    }

    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
           database = FirebaseDatabase.getInstance()
            setUpDrawer()
            binding.toolbar.inflateMenu(R.menu.action_bar)
            binding.toolbar.setTitle("Active Requests")
            binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            binding.toolbar.setNavigationOnClickListener {
                if(binding.drawerLayout.isDrawerOpen(binding.drawerView)) {
                    binding.drawerLayout.close()
                }
                else {
                    binding.drawerLayout.open()
                }
            }
        binding.drawerView.setNavigationItemSelectedListener(object: NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.bookmark_ -> {
                        binding.drawerLayout.close()
                        item.isChecked = false
                        findNavController().navigate(R.id.action_ngoHomeFragment_to_bookMarksFragment)
                    }
                    R.id.nav_logout -> {
                        signOutUser()
                    }
                    R.id.nav_account -> {
                        binding.drawerLayout.close()
                        findNavController().navigate(R.id.action_ngoHomeFragment_to_ngoProfile)
                    }

                }
                return true
            }

        })
            binding.toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item?.itemId) {
                        R.id.actionSearch ->  {
                            val searchView = item.actionView as SearchView
                            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    filterRecycler(newText)
                                    return false
                                }

                            })
                        }
                    }
                    return true
                }

            })


            binding.pb.visibility = View.VISIBLE

            reference = database.reference.child("common")
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

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.action_bar, menu)
//
//        val searchItem = menu.findItem(R.id.actionSearch)
//        val bookmarkItem = menu.findItem(R.id.bookmarks)
//        bookmarkItem.setOnMenuItemClickListener(object: MenuItem.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                when(item?.itemId) {
//                    R.id.bookmarks -> findNavController().navigate(R.id.action_ngoHomeFragment_to_bookMarksFragment)
//                }
//                return true
//            }
//
//        })
//        val searchView = searchItem.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterRecycler(newText)
//                return false
//
//            }
//
//        })
//    }




private fun setUpDrawer() {

     actionDrawerToggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout,
        R.string.nv_open, R.string.nv_close)

    binding.drawerLayout.addDrawerListener(actionDrawerToggle)
    actionDrawerToggle.syncState()


}
    private fun filterRecycler(text: String?) {

        filterList = mutableListOf()
        for(item: ModelClasses in userList) {
            if(item.address.contains(text.toString()) || item.title.contains(text.toString())) {
                filterList.add(item)
            }
        }

        if(filterList.isEmpty())  {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
        }
        else {
            adapter.submitList(filterList)
            binding.ngoRecyclerView.adapter?.notifyDataSetChanged()
        }

    }

    private fun signOutUser() {
        auth.signOut()
            findNavController().navigate(R.id.action_ngoHomeFragment_to_registerMainFragment)
    }

}

