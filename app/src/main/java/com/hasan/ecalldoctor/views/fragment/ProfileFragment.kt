package com.hasan.ecalldoctor.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.FragmentProfileBinding
import com.hasan.ecalldoctor.models.User
import com.hasan.ecalldoctor.myConstants.MyConstants


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        navController = findNavController()

        val currentUserId = FirebaseAuth.getInstance().currentUser.uid

        val profileRef = MyConstants.DB_REF.child("user").child(currentUserId)
        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    if (user != null){
                        binding.profileNameTV.text = user.firstName
                        binding.profileEmilTV.text = user.email
                        binding.profileContactTV.text = user.phoneNumber
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.editProfileBtn.setOnClickListener{
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }


    }

}