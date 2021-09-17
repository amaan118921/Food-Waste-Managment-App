package com.example.foodwastemanagmentapp.restaurants

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.FragmentViewRequestBinding
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory


class ViewRequestFragment : Fragment() {
  private lateinit var binding: FragmentViewRequestBinding
  private val model: CommonViewModel by activityViewModels{
      CommonViewModelFactory(
          (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
      )
  }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewRequestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       binding.titleAdd.setText(model.getData().title)
        binding.addressAdd.setText(model.getData().address)
        binding.descriptionAdd.setText(model.getData().desc)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.done.setOnClickListener {
            findNavController().navigate(R.id.action_viewRequestFragment_to_restaurantHomeFragment)
        }
    }

}