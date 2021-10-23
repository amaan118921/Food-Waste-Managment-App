package com.example.foodwastemanagmentapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.dataClasses.User
import com.example.foodwastemanagmentapp.databinding.FragmentProfileBinding
import com.example.foodwastemanagmentapp.room.UserInfo
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
import kotlinx.coroutines.withContext

class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private  var user: UserInfo? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var uri: String
    private lateinit var unqiue: String
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
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("users").child(auth.uid!!)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                GlobalScope.launch(Dispatchers.IO) {


                 email = snapshot.child("email").value.toString()
                 phone = snapshot.child("phone").value.toString()
                 name = snapshot.child("name").value.toString()
                 uri = snapshot.child("uri").value.toString()

                withContext(Dispatchers.Main) {

                    Picasso.get().load(uri.toUri()).into(binding.profileImage)
                    binding.phoneNo.setText(phone)
                    binding.name.setText(name)
                }

                }

//                binding.emailId.setText(email)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }



        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.save.setOnClickListener {
            val name = binding.name.text.toString()
            val phone = binding.phoneNo.text.toString()
            if(name!=this.name || phone!=this.phone) {
                updateInFirebase(name, phone)
            }
            else {
                Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateInFirebase(name: String, phone: String) {
        val user = UserInfo(name = name, phone = phone, uri = uri, email = email)
        GlobalScope.launch(Dispatchers.IO) {
            val ref = database.reference.child("users").child(auth.uid!!)
            ref.setValue(user).addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful) {
                        Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

    }

}