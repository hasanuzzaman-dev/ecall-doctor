package com.hasan.ecalldoctor.views.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hasan.ecalldoctor.R
import com.hasan.ecalldoctor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set drawer layout
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.fragment)
        binding.navigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? ->

            with(binding){
                when (destination.id) {
                    /*R.id.customerMapsFragment -> {
                        toolbar.visibility = View.GONE
                    }*/
                    R.id.signInFragment -> {
                        toolbar.visibility = View.GONE
                    }
                    else -> {
                        toolbar.visibility = View.VISIBLE
                    }
                }
            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
// 5555215556
// 5555215555
// 5555215554