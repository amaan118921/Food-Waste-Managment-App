package com.example.foodwastemanagmentapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodwastemanagmentapp.databinding.FragmentLogInBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private val navArgs: LogInFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("check").child("key")
        val id = navArgs.id

        binding.logInButton.setOnClickListener {
            email = binding.email.text.toString()
            pass= binding.pass.text.toString()
            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Email and Password", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.pb.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            binding.pb.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Welcome", Toast.LENGTH_SHORT).show()
                            when(id) {
                                1 ->  {
//                                    ref.setValue("10").addOnCompleteListener(object: OnCompleteListener<Void> {
//                                        override fun onComplete(p0: Task<Void>) {
//                                            Toast.makeText(requireContext(), "check value set", Toast.LENGTH_SHORT).show()
//                                            findNavController().navigate(R.id.action_logInFragment_to_restaurantHomeFragment)
//                                        }
//
//                                    })
                                    val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                                    with(sharedPreferences.edit()) {
                                        putString("key", "10")
                                        apply()
                                    }
                                    findNavController().navigate(R.id.action_logInFragment_to_restaurantHomeFragment)

                                }

                                -1 ->   {
//                                    ref.setValue("20").addOnCompleteListener(object: OnCompleteListener<Void> {
//                                        override fun onComplete(p0: Task<Void>) {
//                                            Toast.makeText(requireContext(), "check value set", Toast.LENGTH_SHORT).show()
//                                            findNavController().navigate(R.id.action_logInFragment_to_ngoHomeFragment)
//                                        }
//
//                                    })

                                    val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                                    with(sharedPreferences.edit()) {
                                        putString("key", "20")
                                        apply()
                                    }

                                    findNavController().navigate(R.id.action_logInFragment_to_ngoHomeFragment)

                                }
                            }


                        } else {
                            binding.pb.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}