package com.example.foodwastemanagmentapp.ngo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.databinding.FragmentNgoProfileBinding
import com.example.foodwastemanagmentapp.room.UserInfo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NgoProfile : Fragment() {
    private lateinit var binding: FragmentNgoProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var uri: String
    private lateinit var uniqueId: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNgoProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        GlobalScope.launch(Dispatchers.IO) {
            getUser()
        }

        binding.saveBtn!!.setOnClickListener {
            val name = binding.ngoName.text.toString()
            val phone = binding.ngoPhone.text.toString()
            if(name!= this.name || phone != this.phone) {
                binding.pb!!.visibility = View.VISIBLE
                updateUser(name, phone)
            }
            else  {
                Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            }

        }

        binding.back!!.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun getUser() {
        ref = database.reference.child("ngoUsers").child(auth.uid!!)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              name = snapshot.child("name").value.toString()
              phone = snapshot.child("phone").value.toString()
              uri = snapshot.child("uri").value.toString()
              email = snapshot.child("email").value.toString()
              uniqueId = snapshot.child("unique").value.toString()
                bindData()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun bindData() {
        binding.ngoName.setText(name)
        binding.ngoPhone.setText(phone)
        Picasso.get().load(uri).into(binding.profileImage)
        binding.pb!!.visibility = View.INVISIBLE
    }

    private fun updateUser(name: String, phone: String) {
        val ref = database.reference.child("ngoUsers").child(auth.uid!!)
        val user = UserInfo(name = name, phone = phone, email = email, uri = uri, unique = uniqueId)
        ref.setValue(user).addOnCompleteListener(object: OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                if(p0.isSuccessful) {
                    Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                    binding.pb!!.visibility = View.INVISIBLE
                }
            }

        })
    }



}