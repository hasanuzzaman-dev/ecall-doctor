package com.hasan.ecalldoctor.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.FragmentSignInBinding
import com.hasan.ecalldoctor.models.User
import com.hasan.ecalldoctor.myConstants.MyConstants

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    companion object {
        private const val TAG = "SignInFragment"
    }

    private lateinit var binding: FragmentSignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var phoneNumber: String
    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        navController = findNavController()

        firebaseAuth = FirebaseAuth.getInstance()
        binding.nextBtn.setOnClickListener {


            phoneNumber = binding.phoneET.editText!!.text.toString()

            if (!validatePhoneNumber()) {
                return@setOnClickListener
            }

            binding.progressbar.visibility = View.VISIBLE

            phoneNumber = "+880$phoneNumber"
            Log.d(TAG, "onViewCreated: phoneNumber: $phoneNumber")

            val phoneNumberQuery = MyConstants.DB_REF.child("user")
                    .orderByChild("phoneNumber")
                    .equalTo(phoneNumber)

            phoneNumberQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val flag = snapshot.childrenCount
                        gotoVerify(flag)
                    }else{
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(activity, "You don't have account please register first!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }

        binding.gotoUserSignUPFragmentTV.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToDriverSignUpFragment()
            navController.navigate(action)

        }
    }

    private fun gotoVerify(flag: Long) {
        if (flag > 0){

            val user = User()
            user.phoneNumber = phoneNumber
            user.isNewRegistered = false
            val action = SignInFragmentDirections.actionSignInFragmentToVerifyPhoneFragment(user)
            //action.phoneNumber = phoneNumber
            navController.navigate(action)
            Log.d(TAG, "Phone Number : $phoneNumber")
            binding.progressbar.visibility = View.GONE
        }


    }



    private fun validatePhoneNumber(): Boolean {
        phoneNumber = binding.phoneET.editText!!.text.toString().trim()
        return if (phoneNumber.isEmpty()) {
            binding.phoneET.error = "Field can't be empty!"
            false
        } else {
            binding.phoneET.error = null
            true
        }
    }
}