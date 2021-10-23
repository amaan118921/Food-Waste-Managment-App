package com.example.foodwastemanagmentapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodwastemanagmentapp.databinding.FragmentCheckBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CheckFragment : Fragment() {
   private lateinit var binding: FragmentCheckBinding
    private val navArgs: LogInFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val id = navArgs.id

        sharedPreferences =  activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val key = sharedPreferences.getString("key", "20")

        if(id==10) {
            binding.ngo.visibility = View.INVISIBLE
            binding.hotel.visibility = View.VISIBLE
            if(auth.uid!=null) {



                if(key == id.toString()) {
                            binding.pb.visibility=View.INVISIBLE
                            try {
                                findNavController().navigate(R.id.action_checkFragment_to_restaurantHomeFragment)
                            }
                            catch (e: Exception) {

                            }

                        }
                        else {
                            auth.signOut()
                            try {
                                findNavController().navigate(R.id.action_checkFragment_to_enterProfileFragment)
                            }
                            catch (e: Exception)  {

                            }

                        }

//                ref = database.reference.child("check")
//                ref.addValueEventListener(object: ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val key = snapshot.child("key").value.toString()
//                        if(key == id.toString()) {
//                            binding.pb.visibility=View.INVISIBLE
//                            try {
//                                findNavController().navigate(R.id.action_checkFragment_to_restaurantHomeFragment)
//                            }
//                            catch (e: Exception) {
//
//                            }
//
//                        }
//                        else {
//                            auth.signOut()
//                            try {
//                                findNavController().navigate(R.id.action_checkFragment_to_enterProfileFragment)
//                            }
//                            catch (e: Exception)  {
//
//                            }
//
//                        }
                    }

//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//                })
//            }
            else {
                findNavController().navigate(R.id.action_checkFragment_to_enterProfileFragment)
            }
        }
        else if(id==20) {
            if(auth.uid!=null) {

                if(key==id.toString()) {
                            try {
                                findNavController().navigate(R.id.action_checkFragment_to_ngoHomeFragment)
                            }
                            catch (e:Exception) {

                            }

                        }
                        else {
                            auth.signOut()
                            try {
                                findNavController().navigate(R.id.action_checkFragment_to_enterDetailsFragment)
                            }
                            catch (e: Exception) {

                            }

                        }
//                ref = database.reference.child("check")
//                ref.addValueEventListener(object: ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val key  = snapshot.child("key").value.toString()
//                        if(key==id.toString()) {
//                            try {
//                                findNavController().navigate(R.id.action_checkFragment_to_ngoHomeFragment)
//                            }
//                            catch (e:Exception) {
//
//                            }
//
//                        }
//                        else {
//                            auth.signOut()
//                            try {
//                                findNavController().navigate(R.id.action_checkFragment_to_enterDetailsFragment)
//                            }
//                            catch (e: Exception) {
//
//                            }
//
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                })
            }
            else {
                findNavController().navigate(R.id.action_checkFragment_to_enterDetailsFragment)
            }
        }
    }

}