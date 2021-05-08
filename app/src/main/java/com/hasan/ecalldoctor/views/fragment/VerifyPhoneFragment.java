package com.hasan.ecalldoctor.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.NotNull;
import com.hasan.ecalldoctor.databinding.FragmentVerifyPhoneBinding;
import com.hasan.ecalldoctor.models.User;
import com.hasan.ecalldoctor.myConstants.MyConstants;

import java.util.concurrent.TimeUnit;


public class VerifyPhoneFragment extends Fragment {

    private static final String TAG = "VerifyPhoneFragment";
    private NavController navController;
    private FragmentVerifyPhoneBinding binding;
    private FirebaseAuth firebaseAuth;
    private Context context;
    private String verificationCodeBySystem;
    private String phoneNumber;
    private User user;

    private DatabaseReference databaseReference;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;


    public VerifyPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_verify_phone, container, false);
        binding = FragmentVerifyPhoneBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();


        binding.verifyBtn.setOnClickListener(view1 -> {
            String otp = binding.otpView.getText().toString();
            Log.d(TAG, "OTP : " + otp);

            binding.progressbar.setVisibility(View.VISIBLE);
            verifyCode(otp);

        });


       /* binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                binding.progressbar.setVisibility(View.VISIBLE);
                verifyCode(otp);
            }
        });*/


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG, "onVerificationFailed: Invalid request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG, "onVerificationFailed: The SMS quota for the project has been exceeded");
                }

                // Show a message and update the UI
                // ...

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Log.d(TAG, "onCodeSent: otp"+s);
                verificationCodeBySystem = s;
            }
        };


        if (getArguments() != null) {

            VerifyPhoneFragmentArgs args = VerifyPhoneFragmentArgs.fromBundle(getArguments());
            user = args.getUser();

            phoneNumber = args.getUser().getPhoneNumber();

            Log.d(TAG, "Phone Number: " + phoneNumber);
            PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(callbacks)
                    .build();

            PhoneAuthProvider.verifyPhoneNumber(authOptions);

            /*
            verifyViewModel.getPatientIsRegistered(phoneNumber).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    isRegistered = aBoolean;

                    Log.d(TAG, "isRegistered: "+isRegistered);

                    Log.d(TAG, "Phone Number: "+args.getPhoneNumber());
                    PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(args.getPhoneNumber())
                            .setTimeout(120L, TimeUnit.SECONDS)
                            .setActivity(requireActivity())
                            .setCallbacks(callbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(authOptions);
                }
            });*/


        }








    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            if (user.isNewRegistered()) {
                                user.setUserId(userId);
                                DatabaseReference userRef = MyConstants.DB_REF.child("user");


                                userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "onComplete: " + user.toString());
                                        databaseReference =  MyConstants.DB_REF.child("RegisteredUserId").child("driver").child(userId);
                                        databaseReference.setValue(true);
                                    }
                                });
                            }

                            NavDirections action = VerifyPhoneFragmentDirections.actionVerifyPhoneFragmentToHomeFragment();
                            navController.navigate(action);


                            // or navController.navigate(R.id.action_verifyPhoneFragment_to_homeFragment);


                            //boolean isRegistered = false;

                          /*  if (isRegistered){
                                navController.navigate(R.id.action_verifyPhoneFragment_to_homeFragment);
                            }else {

                                VerifyPhoneFragmentDirections.ActionVerifyPhoneFragmentToSignUpFragment verifyPhoneFragmentToSignUpFragment = VerifyPhoneFragmentDirections
                                        .actionVerifyPhoneFragmentToSignUpFragment();
                                verifyPhoneFragmentToSignUpFragment.setPhoneNumber(phoneNumber);
                                navController.navigate(verifyPhoneFragmentToSignUpFragment);

                            }
*/
                            //navController.navigate(R.id.action_verifyPhoneFragment_to_homeFragment);
                            // ...
                        } else {
                            binding.progressbar.setVisibility(View.GONE);
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.d(TAG, "The verification code entered was invalid");
                                Toast.makeText(context, "The verification code entered was invalid!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void verifyCode(String code) {

        Log.d(TAG, "verifyCode: started");

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);

    }
}