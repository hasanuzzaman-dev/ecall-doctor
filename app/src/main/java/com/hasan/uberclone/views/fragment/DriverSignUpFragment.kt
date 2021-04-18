package com.hasan.uberclone.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.uberclone.R
import com.hasan.uberclone.databinding.FragmentDriverSignUpBinding
import com.hasan.uberclone.models.User

class DriverSignUpFragment : Fragment(R.layout.fragment_driver_sign_up) {

    private lateinit var binding: FragmentDriverSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDriverSignUpBinding.bind(view)
        val navController = findNavController()

        with(binding) {

            signUpBtn.setOnClickListener(View.OnClickListener {
                binding.progressbar.visibility = View.VISIBLE


                val phoneNumber = phoneET.editText?.text.toString()
                val gender = genderAutoTextView.text.toString()
                val dateOfBirth = dateOfBirthTextInputEditText.editText?.text.toString()

                val termsAndCondition = radioButton.isChecked
                val user = User()
                user.firstName = firstNameET.editText?.text.toString()
                user.lastName = lastNameET.editText?.text.toString()
                user.email = emailET.editText?.text.toString()
                user.phoneNumber = phoneNumber
                user.gender = gender
                user.dateOfBirth = dateOfBirth
                user.userType =  "driver"
                user.isTermsAndCondition = termsAndCondition
                user.isNewRegistered = true


                val action =
                        DriverSignUpFragmentDirections.actionDriverSignUpFragmentToVerifyPhoneFragment(user)
                navController.navigate(action)

                Log.d(TAG, "onViewCreated: User : ${user.toString()}")
            })

        }
    }

    companion object {
        private const val TAG = "DriverSignUpFragment"
    }

}