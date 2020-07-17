package com.huawei.hinewsevents.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.hinewsevents.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBarAndController()
        } // Else, need to wait for onRestoreInstanceState

    }

    //We should add Bottom Navigation Bar Menu structure into this activity.

    private fun setupBottomNavigationBarAndController() {

        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        navController.navigateUp()

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
        setOf(R.id.navigation_home, R.id.navigation_bookmark, R.id.navigation_profile ))
        //        , R.id.homeDetailFragment, R.id.bookmarkDetailFragment, R.id.profileDetailFragment ))

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView.setupWithNavController(navController)
        // another method
        //bottomNavView.let { NavigationUI.setupWithNavController(it,navController) }
    }




    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBarAndController()
    }

    //Setting Up the back button
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
        // TODO try for setupBottomNavigationBarAdvance
        // return currentNavController?.value?.navigateUp() ?: false
    }

    // TODO try for setupBottomNavigationBarAdvance
    private fun setupBottomNavigationBarAdvance() {

        //val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        //navController = findNavController(R.id.nav_host_fragment)

        // TODO try navigation advance controller and navigate operations
        val navGraphIds = listOf( R.navigation.home, R.navigation.bookmark, R.navigation.profile )
        val controller = bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment, // change with nav_host_container from FragmentContainerView
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }


}
