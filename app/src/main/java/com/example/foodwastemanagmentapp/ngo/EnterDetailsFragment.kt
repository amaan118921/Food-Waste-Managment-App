package com.example.foodwastemanagmentapp.ngo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentEnterDetailsBinding
import com.example.foodwastemanagmentapp.room.UserInfo
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.net.URI
import kotlin.properties.Delegates

class EnterDetailsFragment : Fragment() {
    private lateinit var binding: FragmentEnterDetailsBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var uri: Uri
    private lateinit var pass: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private var checkValue = "20"
    private var count by Delegates.notNull<Int>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth.signOut()
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent?  = result.data
                if(data!=null) {
                    uri = data.data!!
                    binding.profile.setImageURI(uri)
                }

            }

        }

        fun galleryCall() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            launcher.launch(Intent.createChooser(intent, "Choose Image"))
        }

        binding.profile.setOnClickListener {
            galleryCall()
        }
        binding.logIn.setOnClickListener {
            val action = EnterDetailsFragmentDirections.actionEnterDetailsFragmentToLogInFragment(-1)
            findNavController().navigate(action)
        }

        binding.signUpBtn.setOnClickListener {
            name = binding.resNameText.text.toString()
            phone = binding.phoneEdtText.text.toString()
            email = binding.email.text.toString()
            pass = binding.pass.text.toString()

            if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty())  {
                Toast.makeText(requireContext(), " Enter Sufficient Details", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.pb.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this.requireActivity()) { task->
                    if(task.isSuccessful) {

//                        ref = database.reference.child("check").child("key")
//                        ref.setValue(checkValue).addOnCompleteListener(object: OnCompleteListener<Void> {
//                            override fun onComplete(p0: Task<Void>) {
//                                if(p0.isSuccessful)  {
//                                    Toast.makeText(requireContext(), "Check Value Set", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//
//                        })



                        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                        with(sharedPreferences.edit()) {
                            putString("key", checkValue)
                            apply()

                        }
                        ref = database.reference.child("ngoUsers").child(auth.uid!!)
                        storageReference = storage.reference.child("userImages").child(auth.uid!!)
                        storageReference.putFile(uri).addOnCompleteListener(object: OnCompleteListener<UploadTask.TaskSnapshot> {
                            override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                                if(p0.isSuccessful) {
                                    storageReference.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
                                        override fun onSuccess(p0: Uri?) {
                                            val userUri = p0.toString()
                                            val uniqueId = auth.uid?.substring(0, 4)
                                            val user = UserInfo(name = name, phone = phone, uri = userUri, email = email, unique = uniqueId!!)
                                            ref.setValue(user).addOnCompleteListener(object: OnCompleteListener<Void> {
                                                override fun onComplete(p0: Task<Void>) {
                                                    if(p0.isSuccessful) {
                                                        binding.pb.visibility = View.INVISIBLE
                                                        Toast.makeText(requireContext(), "Welcome", Toast.LENGTH_SHORT).show()
                                                        findNavController().navigate(R.id.action_enterDetailsFragment_to_ngoHomeFragment)
                                                    }
                                                }

                                            })
                                        }

                                    })
                                }
                            }

                        })

                    }
                    else {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                    
                }

            }
        }

    }


}