package com.hasan.uberclone.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hasan.uberclone.R
import com.hasan.uberclone.databinding.FragmentSignInBinding
import com.hasan.uberclone.models.User

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        val navController = findNavController()

        firebaseAuth = FirebaseAuth.getInstance()
        binding.nextBtn.setOnClickListener {

            val countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus
            val number = binding.phoneET.editText!!.text.toString()

            /*String phoneNumber = "+15555215554";

            SignInFragmentDirections.ActionSignInFragmentToVerifyPhoneFragment action = SignInFragmentDirections
                    .actionSignInFragmentToVerifyPhoneFragment();
            action.setPhoneNumber(phoneNumber);
            navController.navigate(action);*/

            if (countryCode != null && number.isNotEmpty()) {
                val phoneNumber = countryCode + number
                val user = User()
                user.phoneNumber = phoneNumber
                user.isNewRegistered = false
                val action = SignInFragmentDirections.actionSignInFragmentToVerifyPhoneFragment(user)
                //action.phoneNumber = phoneNumber
                navController.navigate(action)
                Log.d(TAG, "Phone Number : $phoneNumber")

                /*SignInFragmentDirections action = SignInFragmentDirections.actionSignInFragmentToVerifyPhoneFragment();
                    action.setPhoneNumber(phoneNumber);
                    navController.navigate(action);

                    Log.d(TAG, "Phone Number : " + phoneNumber);*/
            }
        }
        binding.gotoDriverSignUPFragmentTV.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToDriverSignUpFragment()
            navController.navigate(action)
        }

        binding.gotoUserSignUPFragmentTV.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToUserSignUpFragment()
            navController.navigate(action)

        }
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}