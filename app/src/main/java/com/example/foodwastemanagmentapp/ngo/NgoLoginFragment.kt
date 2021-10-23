package com.example.foodwastemanagmentapp.ngo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class NgoLoginFragment: Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

//        binding.next.setOnClickListener {
//            email = binding.email.editText?.text.toString()
//            pass = binding.pass.editText?.text.toString()
//
//            if(email.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(requireContext(), "Enter Email and Password", Toast.LENGTH_SHORT).show()
//            }
//
//            else {
//                binding.pb.visibility = View.VISIBLE
//                auth.signInWithEmailAndPassword(email, pass)
//                    .addOnCompleteListener(this.requireActivity()) { task ->
//                        if (task.isSuccessful) {
//                            binding.pb.visibility = View.INVISIBLE
//                            Toast.makeText(requireContext(), "Welcome", Toast.LENGTH_SHORT).show()
//                            findNavController().navigate(R.id.action_logInFragment_to_ngoHomeFragment)
//
//                        } else {
//                            binding.pb.visibility = View.INVISIBLE
//                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            }
//        }
    }

}