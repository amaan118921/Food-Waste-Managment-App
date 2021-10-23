package com.example.foodwastemanagmentapp.restaurants

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentAddRequestBinding
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
import kotlinx.coroutines.withContext


class AddRequestFragment : Fragment() {


    companion object AddRequest {
        @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentAddRequestBinding

    }

    private var defaultStatus = "Available"
    private lateinit var auth: FirebaseAuth
    private lateinit var lat: String
    private lateinit var lon: String
    private lateinit var name: String
    private lateinit var phone: String
    val list = (1..900000)
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var reference: DatabaseReference
    private val model: CommonViewModel by activityViewModels{
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddRequestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        getDetails()
        databaseReference = database.reference.child("common")
        reference = database.reference.child("requests").child(auth.uid!!)
        binding.apply {
//            back.setOnClickListener {
//                findNavController().navigateUp()
//            }
//            done.setOnClickListener {
//
//            }


        }

        binding.addLocation.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage(getString(R.string.message))
                .setNeutralButton(R.string.Ok) { dialog, which->
                    dialog.dismiss()
                    getLocation()
                }
                .show()


        }



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
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>) {
                        if(p0.isSuccessful) {
                            lat = p0.result?.latitude.toString()
                            lon = p0.result?.longitude.toString()
                            binding.pb.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Location Added", Toast.LENGTH_SHORT).show()
                        }
                }

            })
        }

        else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                0 )
            binding.pb.visibility = View.VISIBLE
        }

    }
    private fun setUpToolbar() {
        binding.toolbar.inflateMenu(R.menu.add_req)
        binding.toolbar.setTitle("New request")
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.done_ -> {
                            addRequest()
                    }
                }
                return true
            }

        })
    }

    private fun getDetails() {
        val ref = database.reference.child("users").child(auth.uid!!)

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").value.toString()
                phone = snapshot.child("phone").value.toString()
                binding.titleAdd.setText(name)
                binding.contact.setText(phone)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun addRequest() {
        val title = binding.titleAdd.editableText.toString()
        val add = binding.addressAdd.editableText.toString()
        val description = binding.descriptionAdd.editableText.toString()
        val phone = binding.contact.editableText.toString()
        if(title.isBlank() || description.isBlank() || add.isBlank() || phone.isBlank() || lat.isBlank() || lon.isBlank()) {
            Toast.makeText(requireContext(), "Add Sufficient Details", Toast.LENGTH_SHORT).show()
        }
        else {
            binding.pb.visibility=View.VISIBLE
            model.setTitle(title)
            model.setAddress(add)
            model.setDescription(description)
            val data = ModelClasses(userId = list.random().toString(), title = title, address = add, desc = description, date = "10 Sep",
                contact = phone, status = defaultStatus, lat = lat, lon = lon)
            databaseReference.push().setValue(data).addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful) {
                        reference.push().setValue(data).addOnCompleteListener(object: OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful) {
                                    binding.pb.visibility=View.INVISIBLE
                                    findNavController().navigate(R.id.action_addRequestFragment_to_restaurantHomeFragment)
                                }
                            }

                        })
                    }
                }

            })


        }
    }
}