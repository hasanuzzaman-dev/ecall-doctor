package com.hasan.uberclone.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hasan.uberclone.databinding.FragmentSignInBinding;

public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";
    private NavController navController;
    private Context context;
    private FragmentSignInBinding binding;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;


    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sign_in, container, false);

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.nextBtn.setOnClickListener(view1 -> {

            String countryCode = binding.countryCodePicker.getSelectedCountryCodeWithPlus();
            String number = binding.phoneET.getEditText().getText().toString();

            /*String phoneNumber = "+15555215554";

            SignInFragmentDirections.ActionSignInFragmentToVerifyPhoneFragment action = SignInFragmentDirections
                    .actionSignInFragmentToVerifyPhoneFragment();
            action.setPhoneNumber(phoneNumber);
            navController.navigate(action);*/

            if (countryCode != null && !number.isEmpty()) {

                String phoneNumber = countryCode + number;


                SignInFragmentDirections.ActionSignInFragmentToVerifyPhoneFragment action = SignInFragmentDirections.actionSignInFragmentToVerifyPhoneFragment();
                action.setPhoneNumber(phoneNumber);
                navController.navigate(action);

                Log.d(TAG, "Phone Number : " + phoneNumber);
                /*SignInFragmentDirections action = SignInFragmentDirections.actionSignInFragmentToVerifyPhoneFragment();
                action.setPhoneNumber(phoneNumber);
                navController.navigate(action);

                Log.d(TAG, "Phone Number : " + phoneNumber);*/
            }

        });

        binding.gotoDriverSignUPFragmentTV.setOnClickListener(view1 -> {

        });


    }
}