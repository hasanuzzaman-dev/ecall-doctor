package com.hasan.ecalldoctor.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.FragmentSettingBinding


class SettingFragment : Fragment(R.layout.fragment_setting) {

    companion object{
        private const val TAG = "SettingFragment"
    }

    private lateinit var navController: NavController
    private lateinit var binding: FragmentSettingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingBinding.bind(view)
        navController = findNavController()


        with(binding){
            logoutBtn.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(R.id.action_settingFragment_to_signInFragment)
            }
        }
    }


}