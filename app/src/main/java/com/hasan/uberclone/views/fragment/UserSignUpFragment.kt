package com.hasan.uberclone.views.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.hasan.uberclone.R
import com.hasan.uberclone.databinding.FragmentUserSignUpBinding
import com.hasan.uberclone.models.User

class UserSignUpFragment : Fragment(R.layout.fragment_user_sign_up) {

    private lateinit var binding: FragmentUserSignUpBinding

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserSignUpBinding.bind(view)
        val navController = findNavController()

        with(binding) {

            signUpBtn.setOnClickListener(View.OnClickListener {
                /*  if (!validateFirstName() or !validateLastName() or !validateEmail()) {
                      return@OnClickListener
                  }*/

                if (!validateFirstName() or !validateLastName() or !validateEmail()) {
                    return@OnClickListener
                }
                binding.progressbar.visibility = View.VISIBLE


                val phoneNumber = phoneET.editText?.text.toString()
                val gender = genderAutoTextView.text.toString()
                val dateOfBirth = dateOfBirthTextInputEditText.editText?.text.toString()
                val inputType = "customer"
                val termsAndCondition = radioButton.isChecked
                val user = User()
                user.firstName = firstName
                user.lastName = lastName
                user.email = email
                user.phoneNumber = phoneNumber
                user.gender = gender
                user.dateOfBirth = dateOfBirth
                user.userType = inputType
                user.isTermsAndCondition = termsAndCondition
                user.isNewRegistered = true


                val action = UserSignUpFragmentDirections.actionUserSignUpFragmentToVerifyPhoneFragment(user)
                navController.navigate(action)

                Log.d(TAG, "onViewCreated: User : ${user.toString()}")


            })

        }

    }

    private fun validateFirstName(): Boolean {
        firstName = binding.firstNameET.editText!!.text.toString().trim()
        return if (firstName.isEmpty()) {
            binding.firstNameET.error = "Field can't be empty!"
            false
        } else {
            binding.firstNameET.error = null
            true
        }
    }

    private fun validateLastName(): Boolean {
        lastName = binding.lastNameET.editText!!.text.toString().trim()
        return if (lastName.isEmpty()) {
            binding.lastNameET.error = "Field can't be empty!"
            false
        } else {
            binding.lastNameET.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        email = binding.emailET.editText!!.text.toString().trim()
        return if (email.isEmpty()) {
            binding.emailET.error = "Field can't be empty!"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailET.error = "Please enter a valid email address!"
            false
        } else {
            binding.emailET.error = null
            true
        }
    }

    companion object {
        private const val TAG = "UserSignUpFragment"
    }
}