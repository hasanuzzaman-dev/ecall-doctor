package com.hasan.uberclone.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hasan.uberclone.R;
import com.hasan.uberclone.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFragment";

    private FirebaseAuth firebaseAuth;
    private NavController navController;
    private FragmentHomeBinding binding;
    private Context context;
    private FirebaseUser currentUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.signOutBtn.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            NavDirections navDirections = HomeFragmentDirections.actionHomeFragmentToSignInFragment();
            navController.navigate(navDirections);
        });

        binding.driverBtn.setOnClickListener(view1 -> navController.navigate(R.id.driverMapsFragment));
        binding.customerBtn.setOnClickListener(view1 -> navController.navigate(R.id.customerMapsFragment));


    }

    @Override
    public void onStart() {
        super.onStart();

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            NavDirections navDirections = HomeFragmentDirections.actionHomeFragmentToSignInFragment();
            navController.navigate(navDirections);
        }
    }
}