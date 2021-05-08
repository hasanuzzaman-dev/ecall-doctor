package com.hasan.ecalldoctor.views.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.FragmentDriverSignUpBinding
import com.hasan.ecalldoctor.models.User

class DriverSignUpFragment : Fragment(R.layout.fragment_driver_sign_up) {

    companion object {
        private const val TAG = "DriverSignUpFragment"
    }

    private lateinit var binding: FragmentDriverSignUpBinding
    private lateinit var navController: NavController

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var materialDatePicker: MaterialDatePicker<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDriverSignUpBinding.bind(view)
        navController = findNavController()

        createDatePicker()

        val genderList = resources.getStringArray(R.array.genderList)
        val genderAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,genderList)
        binding.genderAutoTextView.setAdapter(genderAdapter)

        with(binding) {

            signUpBtn.setOnClickListener(View.OnClickListener {
                binding.progressbar.visibility = View.VISIBLE


                val phoneNumber = phoneET.editText?.text.toString()
                val gender = genderAutoTextView.text.toString()
                val dateOfBirth = dateOfBirthTextInputLayout.editText?.text.toString()

                val termsAndCondition = radioButton.isChecked
                val user = User()
                user.firstName = firstNameET.editText?.text.toString()
                user.lastName = lastNameET.editText?.text.toString()
                user.email = emailET.editText?.text.toString()
                user.phoneNumber = "+880$phoneNumber"
                user.gender = gender
                user.dateOfBirth = dateOfBirth
                user.userType = "driver"
                user.isTermsAndCondition = termsAndCondition
                user.isNewRegistered = true


                val action =
                        DriverSignUpFragmentDirections.actionDriverSignUpFragmentToVerifyPhoneFragment(user)
                navController.navigate(action)

                Log.d(TAG, "onViewCreated: User : ${user.toString()}")
            })

        }

        binding.dateOfBirthTextInputEditText.setOnClickListener{
            materialDatePicker.show(childFragmentManager,materialDatePicker.toString())
        }
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.dateOfBirthTextInputEditText.setText("${materialDatePicker.headerText}")
        }



    }

    private fun createDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select a Date")
        materialDatePicker = builder.build()

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



}