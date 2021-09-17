package com.example.foodwastemanagmentapp.ngo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodwastemanagmentapp.R
import com.example.foodwastemanagmentapp.application.MyApplication
import com.example.foodwastemanagmentapp.databinding.ViewRequestNgoBinding
import com.example.foodwastemanagmentapp.room.ModelClasses
import com.example.foodwastemanagmentapp.viewModel.CommonViewModel
import com.example.foodwastemanagmentapp.viewModel.CommonViewModelFactory

class ViewRequestNgoFragment: Fragment() {
    private lateinit var binding: ViewRequestNgoBinding
    private val model: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            (activity?.application as MyApplication).database.itemDao(), requireParentFragment()
        )
    }
    private lateinit var item: ModelClasses
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewRequestNgoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        item = model.getData()
        binding.titleName.text = item.title
        binding.addressAdd.text = item.address
        binding.date.text = "10 Sep"
        binding.desc.text = item.desc
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.approveReqBtn.setOnClickListener {
            findNavController().navigate(R.id.action_viewRequestNgoFragment_to_ngoHomeFragment)
        }
    }
}