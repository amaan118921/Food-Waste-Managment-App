package com.example.foodwastemanagmentapp.restaurants

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentRestaurantHomeBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class RestaurantHomeFragment : Fragment() {


    private lateinit var adapter: RestaurantHomeAdapter
    private lateinit var auth: FirebaseAuth
    private val model: CommonViewModel by activityViewModels{
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    @SuppressLint("StaticFieldLeak")
    companion object {
         @SuppressLint("StaticFieldLeak")
         lateinit var binding: FragmentRestaurantHomeBinding
    }


//    companion object {
//        fun result(): Boolean {
//            showConfirmationDialog()
//        }
//        fun showConfirmationDialog(): Boolean {
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle(android.R.string.dialog_alert_title)
//                .setMessage(R.string.delete_question)
//                .setCancelable(false)
//                .setNegativeButton(R.string.no) { _, _ -> }
//                .setPositiveButton(R.string.yes) { _, _ ->
//                   result()
//                }
//                .show()
//
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        if(auth.uid == null) {
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_hotelRegisterFragment)
        }
        adapter = RestaurantHomeAdapter(requireParentFragment(), model)
        binding.recyclerView.adapter = adapter
        model.allData.observe(viewLifecycleOwner) { data ->
            data.let {
                adapter.submitList(it)
            }
        }

        binding.logOut.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_hotelRegisterFragment)
            Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show()
        }

        binding.addRequest.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_addRequestFragment)
        }

    }

}