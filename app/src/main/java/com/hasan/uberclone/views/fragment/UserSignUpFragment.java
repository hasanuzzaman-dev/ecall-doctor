package com.hasan.uberclone.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.hasan.uberclone.databinding.FragmentUserSignUpBinding;


public class UserSignUpFragment extends Fragment {


    private static final String TAG = "UserSignUpFragment";
    private NavController navController;
    private Context context;
    private FragmentUserSignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private String firstName, lastName, email;


    public UserSignUpFragment() {
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
        //return inflater.inflate(R.layout.fragment_sign_up, container, false);
        binding = FragmentUserSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateFirstName() | !validateLastName() | !validateEmail()) {
                    return;
                }

                binding.progressbar.setVisibility(View.VISIBLE);

             /*   VerifyPhoneFragmentArgs args = VerifyPhoneFragmentArgs.fromBundle(getArguments());
                String phoneNumber = args.getPhoneNumber();*/

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String currentUserId = firebaseUser.getUid();

                //UserRegistration userRegistration = new UserRegistration(currentUserId, 1, name, phoneNumber, email);

                //  UserRegistration userRegistration = new UserRegistration("RDNdJrLcMBcz3FMWD2nsV4Qbski2", 1, name, "+8801713288634", email);




            }
        });

    }

    private boolean validateFirstName() {
        firstName = binding.firstNameET.getEditText().getText().toString().trim();
        if (firstName.isEmpty()) {
            binding.firstNameET.setError("Field can't be empty!");
            return false;
        } else {
            binding.firstNameET.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        lastName = binding.lastNameET.getEditText().getText().toString().trim();
        if (lastName.isEmpty()) {
            binding.lastNameET.setError("Field can't be empty!");
            return false;
        } else {
            binding.lastNameET.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        email = binding.emailET.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            binding.emailET.setError("Field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailET.setError("Please enter a valid email address!");
            return false;
        } else {
            binding.emailET.setError(null);
            return true;
        }
    }
}