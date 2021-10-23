package com.example.foodwastemanagmentapp.restaurants

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.ContentFrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.dataClasses.Item
import com.example.foodwastemanagmentapp.databinding.FragmentRestaurantHomeBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RestaurantHomeFragment : Fragment() {

    private lateinit var adapter: RestaurantHomeAdapter
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var bottom: BottomNavigationView
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private var list = MutableLiveData<MutableList<ModelClasses>>()
    private lateinit var filterList: MutableList<ModelClasses>
    private val model: CommonViewModel by activityViewModels{
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    @SuppressLint("StaticFieldLeak")
    companion object {
         @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentRestaurantHomeBinding
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpDrawer()
        setUpToolbar()

        binding.pb.visibility = View.VISIBLE
        auth = FirebaseAuth.getInstance()
//        bottom = binding.bottomNavigation


            database = FirebaseDatabase.getInstance()

            list.value = mutableListOf()
            adapter = RestaurantHomeAdapter(requireParentFragment(), model)
            binding.recyclerView.adapter = adapter

                    ref = database.reference.child("requests").child(auth.uid!!)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.value!!.clear()
                for(dataSnapshot: DataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(ModelClasses::class.java)
                    list.value!!.add(data!!)
                }
                check()
                binding.recyclerView.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        adapter.submitList(list.value)






//
//        model.allData.observe(this.viewLifecycleOwner) { data ->
//            data.let {
//                adapter.submitList(it)
//            }
//        }

//        binding.logOut.setOnClickListener {
//            auth.signOut()
//            findNavController().navigate(R.id.action_restaurantHomeFragment_to_registerMainFragment)
//            Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show()
//        }

        binding.addReq.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_addRequestFragment)
        }

//        bottom.setOnNavigationItemSelectedListener {  item ->
//            when(item.itemId) {
//                R.id.profile -> {
//                    findNavController().navigate(R.id.action_restaurantHomeFragment_to_profileFragment)
//                    true
//                }
//                else -> false
//            }
//
//        }

    }


   private fun setUpToolbar()  {
       binding.toolbar.inflateMenu(R.menu.restaurant_home)
       binding.toolbar.setTitle("Your Requests")
       binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
       binding.toolbar.setNavigationOnClickListener {
           binding.resDrawer.open()
       }
       binding.toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
           override fun onMenuItemClick(item: MenuItem?): Boolean {
               when(item?.itemId) {
                   R.id.actionSearch -> {
                       setUpSearchView(item)
                   }
               }
               return true
           }

       })
   }

   private fun setUpSearchView(item: MenuItem?) {
       val searchView = item?.actionView as SearchView
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

    private fun filterRecycler(newText: String?) {
        filterList = mutableListOf()
        for(item: ModelClasses in list.value!!) {
            if(item.address.contains(newText.toString()) || item.title.contains(newText.toString()))  {
                filterList.add(item)
            }
        }

        if(filterList.isEmpty()) {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
        }
        else {
            adapter.submitList(filterList)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

    }

    private fun check() {

        if(list.value!!.isEmpty()) {
            binding.noRequest.visibility = View.VISIBLE
            binding.pb.visibility = View.INVISIBLE
        }
        else {
            binding.noRequest.visibility = View.INVISIBLE
        }
    }

    private fun signOutUser() {
             auth.signOut()
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_registerMainFragment)
            Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show()
    }

private fun setUpDrawer() {
    actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), binding.resDrawer, R.string.nv_open, R.string.nv_close)
    binding.resDrawer.addDrawerListener(actionBarDrawerToggle)
    actionBarDrawerToggle.syncState()
    binding.navView.setNavigationItemSelectedListener(object: NavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.nav_logout -> {
                    signOutUser()
                }
                R.id.nav_account -> {
                    binding.resDrawer.close()
                    findNavController().navigate(R.id.action_restaurantHomeFragment_to_profileFragment)
                }

            }
            return true
        }

    })
}

}