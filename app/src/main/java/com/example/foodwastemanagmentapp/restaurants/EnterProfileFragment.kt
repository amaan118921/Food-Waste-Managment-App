package com.example.foodwastemanagmentapp.restaurants

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentEnterProfileBinding
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
import kotlin.properties.Delegates

class EnterProfileFragment: Fragment() {
    private lateinit var binding: FragmentEnterProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var sharedPreferences: SharedPreferences
    private var count by Delegates.notNull<Int>()
    private val checkValue = "10"
    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var uri: Uri
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterProfileBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
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

            binding.logIn.setOnClickListener {
                val action = EnterProfileFragmentDirections.actionEnterProfileFragmentToLogInFragment(1)
                findNavController().navigate(action)
            }
            binding.profile.setOnClickListener {
                galleryCall()
            }
            binding.signUpBtn.setOnClickListener {

                name = binding.resNameText.editableText.toString()
                email = binding.email.editableText.toString()
                phone = binding.phoneEdtText.editableText.toString()
                pass =  binding.pass.editableText.toString()
                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty() || uri.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Enter Sufficient Details", Toast.LENGTH_SHORT).show()
                }
                else {

                    binding.pb.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this.requireActivity())  { task ->
                        if(task.isSuccessful) {


//                            ref = database.reference.child("check").child("key")
//                            ref.setValue(checkValue.toString()).addOnCompleteListener(object: OnCompleteListener<Void> {
//                                override fun onComplete(p0: Task<Void>) {
//                                    if(p0.isSuccessful) {
//                                        Toast.makeText(requireContext(), "Check Value Set", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//
//                            })


                            sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                            with(sharedPreferences.edit()) {
                                putString("key", checkValue)
                                apply()

                            }
                            Toast.makeText(requireContext(), "Shared Preferences", Toast.LENGTH_SHORT).show()
                            ref = database.reference.child("users").child(auth.uid!!)
                            storageRef = storage.reference.child("userImages").child(auth.uid!!)
                            storageRef.putFile(uri).addOnCompleteListener(object: OnCompleteListener<UploadTask.TaskSnapshot> {
                                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                                    if(p0.isSuccessful) {
                                        storageRef.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                                            override fun onSuccess(p0: Uri?) {
                                                val userUri = p0.toString()
                                                val user = UserInfo(name=name, phone = phone, uri = userUri, email = email)
                                                ref.setValue(user).addOnCompleteListener(object: OnCompleteListener<Void> {
                                                    override fun onComplete(p0: Task<Void>) {
                                                        if(p0.isSuccessful) {
                                                            Toast.makeText(requireContext(), "Registered Successfully", Toast.LENGTH_SHORT).show()
                                                            findNavController().navigate(R.id.action_enterProfileFragment_to_restaurantHomeFragment)
                                                        }
                                                    }

                                                })
                                            }

                                        })
                                    }
                                }

                            })

                        }
                        else  {
                            Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()
                            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }





                }

            }


    }
}