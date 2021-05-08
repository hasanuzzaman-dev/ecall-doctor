package com.hasan.ecalldoctor.views.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.FragmentUpdateProfileBinding
import com.hasan.ecalldoctor.models.User
import com.hasan.ecalldoctor.myConstants.MyConstants


class UpdateProfileFragment : Fragment(R.layout.fragment_update_profile) {

    companion object {
        private const val TAG = "UpdateProfileFragment"
    }

    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var navController: NavController
    private lateinit var materialDatePicker: MaterialDatePicker<*>
    private lateinit var user: User


    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateProfileBinding.bind(view)
        navController = findNavController()

        createDatePicker();

        val currentUserId = FirebaseAuth.getInstance().currentUser.uid

        val genderList = resources.getStringArray(R.array.genderList)
        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genderList)
        binding.genderATV.setAdapter(genderAdapter)

        val bloodGroupList = resources.getStringArray(R.array.bloodGroupList)
        val bloodGroupAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bloodGroupList)
        binding.bloodGroupATV.setAdapter(bloodGroupAdapter)

        val profileRef = MyConstants.DB_REF.child("user/$currentUserId")
        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User::class.java)!!
                    with(binding) {
                        firstNameTextInputEditText.setText(user.firstName)
                        lastNameETTextInputEditText.setText(user.lastName)
                        emailETTextInputEditText.setText(user.email)
                        addressTextInputEditText.setText(user.address)
                        genderATV.setText(user.gender)
                        bloodGroupATV.setText(user.bloodGroup)
                        dateOfBirthTextInputEditText.setText(user.dateOfBirth)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.dateOfBirthTextInputEditText.setOnClickListener {
            materialDatePicker.show(childFragmentManager, materialDatePicker.toString())
        }
        materialDatePicker.addOnPositiveButtonClickListener {
            binding.dateOfBirthTextInputEditText.setText("${materialDatePicker.headerText}")
        }



        binding.updateProfileBtn.setOnClickListener {


            if (!validateFirstName() or !validateLastName() or !validateEmail()) {
                return@setOnClickListener
            }
            binding.progressbar.visibility = View.VISIBLE


            val gender = binding.genderATV.text.toString()
            val dateOfBirth = binding.dateOfBirthET.editText?.text.toString()
            val bloodGroup = binding.bloodGroupATV.text.toString()
            val address = binding.addressTextInputLayout.editText?.text.toString()


            val newUser = User()
            newUser.firstName = firstName
            newUser.lastName = lastName
            newUser.email = email
            newUser.phoneNumber =  user.phoneNumber
            newUser.bloodGroup = bloodGroup
            newUser.address = address
            newUser.gender = gender
            newUser.dateOfBirth = dateOfBirth
            newUser.userType = "customer"
            newUser.isTermsAndCondition = true
            newUser.isNewRegistered = true

            val updateProfileRef = MyConstants.DB_REF.child("user/$currentUserId")
            updateProfileRef.setValue(newUser).addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed()
                }
            })

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