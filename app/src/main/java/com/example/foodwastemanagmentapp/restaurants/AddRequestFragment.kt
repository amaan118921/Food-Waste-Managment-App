package com.example.foodwastemanagmentapp.restaurants

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentAddRequestBinding
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddRequestFragment : Fragment() {


    companion object AddRequest {
        @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentAddRequestBinding

    }



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

        binding.apply {
            back.setOnClickListener {
                findNavController().navigateUp()
            }
            done.setOnClickListener {
                val title = binding.titleAdd.editableText.toString()
                val add = binding.addressAdd.editableText.toString()
                val description = binding.descriptionAdd.editableText.toString()
                if(title.isBlank() || description.isBlank() || add.isBlank()) {
                    Toast.makeText(requireContext(), "Add Sufficient Details", Toast.LENGTH_SHORT).show()
                }
                else {
                    model.setTitle(title)
                    model.setAddress(add)
                    model.setDescription(description)
                    binding.pb.visibility = View.VISIBLE
                    model.insertInRoomAndFirebase(title, add, description, "10 Sep")

                    findNavController().navigate(R.id.action_addRequestFragment_to_restaurantHomeFragment)
                }
            }
        }
    }
}