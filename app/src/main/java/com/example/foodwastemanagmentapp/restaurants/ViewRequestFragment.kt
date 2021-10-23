package com.example.foodwastemanagmentapp.restaurants

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.ListMenuItemView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentViewRequestBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewRequestFragment : Fragment() {
  private lateinit var binding: FragmentViewRequestBinding
  private lateinit var title: String
  private lateinit var desc: String
  private lateinit var address: String
  private lateinit var auth: FirebaseAuth
  private lateinit var database: FirebaseDatabase
  private lateinit var ref: DatabaseReference
  private lateinit var phone: String
  private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
  private lateinit var lat: String
  private lateinit var lon: String
  private lateinit var status: String
  private val model: CommonViewModel by activityViewModels{
      CommonViewModelFactory(
          (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
      )
  }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewRequestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        title = model.getData().title
        address = model.getData().address
        desc = model.getData().desc
        phone = model.getData().contact
        status = model.getData().status
        lat = model.getData().lat
        lon = model.getData().lon
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.titleAdd.setText(title)
        binding.addressAdd.setText(address)
        binding.descriptionAdd.setText(desc)
        binding.contact.setText(phone)
        binding.status.setText(status)
//        binding.back.setOnClickListener {
//            findNavController().navigateUp()
//        }

        binding.addLocation.setOnClickListener {
            getLocation()
        }

//
//        binding.done.setOnClickListener {
//
//        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(object: OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>) {
                    if(p0.isSuccessful) {
                        lat = p0.result?.latitude.toString()
                        lon = p0.result?.longitude.toString()
                        binding.pB.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Location Updated", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                0 )
            binding.pB.visibility = View.VISIBLE
        }

    }

    private fun editRequest() {
        title = binding.titleAdd.text.toString()
        address = binding.addressAdd.text.toString()
        desc = binding.descriptionAdd.text.toString()
        phone = binding.contact.text.toString()
        status = binding.status.text.toString()
        if(title!=model.getData().title || address!=model.getData().address || desc!=model.getData().desc
            || phone != model.getData().contact || status != model.getData().status || lat!= model.getData().lat
            || lon!=model.getData().lon)  {
            binding.pB.visibility=View.VISIBLE
            ref = database.reference.child("requests").child(auth.uid!!)
            val data = ModelClasses(userId = model.getData().userId, title = title, address = address, desc = desc, date = "10 Sep", contact = phone, status = status, lat = lat, lon = lon)
            var query: Query = ref.orderByChild("userId").equalTo(model.getData().userId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataSnapshot: DataSnapshot in snapshot.children) {
                        dataSnapshot.ref.setValue(data).addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(task: Task<Void>) {
                                if(task.isSuccessful) {
                                    ref = database.reference.child("common")
                                    query = ref.orderByChild("userId").equalTo(model.getData().userId)
                                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for(snap: DataSnapshot in snapshot.children) {
                                                snap.ref.setValue(data).addOnCompleteListener(object :OnCompleteListener<Void> {
                                                    override fun onComplete(p0: Task<Void>) {
                                                        if(p0.isSuccessful) {
                                                            binding.pB.visibility=View.INVISIBLE
                                                            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                                                            findNavController().navigate(R.id.action_viewRequestFragment_to_restaurantHomeFragment)
                                                        }
                                                    }

                                                })
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })


                                }
                                else {
                                    binding.pB.visibility=View.INVISIBLE
                                    Toast.makeText(requireContext(), "Cannot Updated", Toast.LENGTH_SHORT).show()
                                    findNavController().navigateUp()
                                }
                            }

                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.pB.visibility=View.INVISIBLE
                    Toast.makeText(requireContext(), "Cannot find the location", Toast.LENGTH_SHORT).show()
                }

            })


        }



    }

    private fun setUpToolbar() {
        binding.toolbar.inflateMenu(R.menu.view_res)
        binding.toolbar.setTitle("Edit request")
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener(object :Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.done_ -> {
                        editRequest()
                    }

                    R.id.delete_over -> {
                        callDialog()

//                        Toast.makeText(requireContext(), "Cannot find the location", Toast.LENGTH_SHORT).show()

//                        GlobalScope.launch(Dispatchers.IO) {
//                            withContext(Dispatchers.Main) {
//                                findNavController().navigate(R.id.action_viewRequestFragment_to_restaurantHomeFragment)
//                            }
//                        }

                    }
                }
                return true
            }

        })
    }
    private fun callDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Warning")
            .setMessage(getString(R.string.delete_question))
            .setPositiveButton(R.string.yes) { dialog, which->
                deleteRequest()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, which->
                dialog.dismiss()
            }
            .show()
    }
    private fun deleteRequest() {
        binding.pB.visibility = View.VISIBLE
        model.deleteInFirebase(model.getData().title)
        findNavController().navigate(R.id.action_viewRequestFragment_to_restaurantHomeFragment)
    }

}