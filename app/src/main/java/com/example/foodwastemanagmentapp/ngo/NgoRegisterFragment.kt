package com.example.foodwastemanagmentapp.ngo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.databinding.FragmentNgoRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import java.util.concurrent.TimeUnit


class NgoRegisterFragment : Fragment() {
    private lateinit var binding: FragmentNgoRegisterBinding
    private lateinit var ccp: CountryCodePicker
    private lateinit var phone: String
    private lateinit var code: String
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var userToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNgoRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ccp = binding.ccp
        ccp.registerPhoneNumberTextView(binding.phoneEdtText)
        auth = FirebaseAuth.getInstance()


        binding.ed1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.ed1.text.length  == 1) {
                    binding.ed2.requestFocus()
                }
            }
        })


        binding.ed2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.ed2.text.length == 1) {
                    binding.ed3.requestFocus()
                }
            }
        })

        binding.ed3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.ed3.text.length== 1) {
                    binding.ed4.requestFocus()
                }
            }
        })

        binding.ed4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.ed4.text.length== 1) {
                    binding.ed5.requestFocus()
                }
            }
        })

        binding.ed5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.ed5.text.length == 1) {
                    binding.ed6.requestFocus()
                }
            }
        })

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(requireContext(),"Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
               if(p0 is FirebaseAuthInvalidCredentialsException) {
                   Toast.makeText(requireContext(),"Invalid Request", Toast.LENGTH_SHORT).show()
               }
                else {
                   Toast.makeText(requireContext(),"SMS Quota Exceeded", Toast.LENGTH_SHORT).show()
               }
                binding.f1.visibility = View.VISIBLE
                binding.verifyButton.visibility = View.INVISIBLE
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                userId = p0
                userToken = p1
            }

        }

        fun getCallbacks() {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }



        binding.sendOtp.setOnClickListener {
            phone = ccp.fullNumberWithPlus
            if(phone.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show()
            }
            else {
                getCallbacks()
                binding.f1.visibility = View.INVISIBLE
                binding.verifyButton.visibility = View.VISIBLE
            }
        }

        binding.verifyButton.setOnClickListener {

            code = (binding.ed1.text.toString() + binding.ed2.text.toString() + binding.ed3.text.toString() + binding.ed4.text.toString()
                        + binding.ed5.text.toString() + binding.ed6.text.toString())

            if(code.length<6) {
                Toast.makeText(requireContext(), "Enter 6 Digit OTP", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.pb.visibility = View.VISIBLE
                val credential = PhoneAuthProvider.getCredential(userId, code)
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(object : OnCompleteListener<AuthResult> {
            override fun onComplete(p0: Task<AuthResult>) {
                if(p0.isSuccessful) {
                    binding.pb.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.ngo)
                }
            }

        })
    }

}