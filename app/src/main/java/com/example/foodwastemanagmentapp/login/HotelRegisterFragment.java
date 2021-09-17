package com.example.foodwastemanagmentapp.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodwastemanagmentapp.R;
import com.example.foodwastemanagmentapp.databinding.FragmentHotelRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class HotelRegisterFragment extends Fragment {

    private FragmentHotelRegisterBinding binding;
    private String phone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuthInvalidCredentialsException obj;
    private String verId;
    private PhoneAuthOptions options;
    private FirebaseAuth auth;
    private String code;
    private PhoneAuthCredential credential;
    private PhoneAuthProvider.ForceResendingToken userToken;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
      binding = FragmentHotelRegisterBinding.inflate(inflater);
      return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        CountryCodePicker ccp = binding.ccp;
        ccp.registerPhoneNumberTextView(binding.phoneEdtText);
        FirebaseApp.initializeApp(requireContext());
        auth = FirebaseAuth.getInstance();
        binding.ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.ed1.getText().length()== 1) {
                    binding.ed2.requestFocus();
                }
            }
        });


        binding.ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.ed2.getText().length()== 1) {
                    binding.ed3.requestFocus();
                }
            }
        });

        binding.ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.ed3.getText().length()== 1) {
                    binding.ed4.requestFocus();
                }
            }
        });

        binding.ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.ed4.getText().length()== 1) {
                    binding.ed5.requestFocus();
                }
            }
        });

        binding.ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.ed5.getText().length()== 1) {
                    binding.ed6.requestFocus();
                }
            }
        });

        binding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = binding.phoneEdtText.getEditableText().toString();
                if(phone.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    binding.f1.setVisibility(View.INVISIBLE);
                    binding.verifyButton.setVisibility(View.VISIBLE);
                    callOptions();
                }
            }
        });

    callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(requireContext(), "Verification Completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
            if(e.equals(obj)) {
                Toast.makeText(requireContext(), "Invalid Request", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(requireContext(), "SMS Quota Exceeded", Toast.LENGTH_SHORT).show();
            }
            binding.f1.setVisibility(View.VISIBLE);
            binding.verifyButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verId = s;
            userToken = forceResendingToken;
        }
    };





        binding.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              code = binding.ed1.getText().toString() + binding.ed2.getText().toString() + binding.ed3.getText().toString() + binding.ed4.getText().toString()
                      + binding.ed5.getText().toString() + binding.ed6.getText().toString();

              if(code.length() < 6) {
                  Toast.makeText(requireContext(), "Enter 6 Digit OTP", Toast.LENGTH_SHORT).show();
              }
              else {
                    binding.pb.setVisibility(View.VISIBLE);
                    credential = PhoneAuthProvider.getCredential(verId, code);
                      signInWithPhoneAuthCredential(credential);
              }
            }
        });







    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            binding.pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(requireContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireView()).navigate(R.id.action_hotelRegisterFragment_to_restaurantHomeFragment);
                        }
                    }
                });
    }

    void callOptions() {
        options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
