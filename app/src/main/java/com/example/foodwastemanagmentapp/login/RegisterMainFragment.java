package com.example.foodwastemanagmentapp.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodwastemanagmentapp.R;
import com.example.foodwastemanagmentapp.databinding.FragmentMainRegisterBinding;

import org.jetbrains.annotations.NotNull;

public class RegisterMainFragment extends Fragment {

    private FragmentMainRegisterBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
       binding = FragmentMainRegisterBinding.inflate(inflater);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        binding.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registerMainFragment_to_restaurantHomeFragment);
            }
        });
        binding.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.action_registerMainFragment_to_ngoHomeFragment2);
            }
        });

    }
}




