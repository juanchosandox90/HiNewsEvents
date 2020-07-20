package com.huawei.hinewsevents.ui.main

import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
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
import com.huawei.hinewsevents.ui.push.PushService
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val HUAWEIID_SIGNIN = 1002
    private lateinit var signOutBtn : TextView
    private lateinit var signInBtn : TextView
    private lateinit var navController: NavController
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getToken()

        if (savedInstanceState == null) {
            setupBottomNavigationBarAndController()
        } // Else, need to wait for onRestoreInstanceState

        signInBtn = findViewById(R.id.button_signin)
        signOutBtn = findViewById(R.id.button_signout)
        initView()
        signIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HUAWEIID_SIGNIN) {
            //login success
            //get user message by parseAuthResultFromIntent
            val authHuaweiIdTask =
                HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful) {
                val huaweiAccount = authHuaweiIdTask.result
                val accessToken = huaweiAccount.accessToken
                val credential = HwIdAuthProvider.credentialWithToken(accessToken)

                AGConnectAuth.getInstance().signIn(credential)
                    .addOnSuccessListener { signInResult -> // onSuccess

                        val user = signInResult.user
                        Toast.makeText(
                            this,
                            "signIn success. Welcome " + user.displayName,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.i(
                            TAG,
                            "signIn success. Huawei Account Details: " + huaweiAccount.toString()
                        )
                        Log.d(
                            TAG,
                            "AccessToken: " + huaweiAccount.accessToken
                        )
                        Log.d(
                            TAG,
                            "IDToken: " + huaweiAccount.idToken
                        )
                        signInBtn.isEnabled = false;
                        signOutBtn.isEnabled = true;
                    }.addOnFailureListener {
                        Toast.makeText(this, "signIn failed: " + it.message, Toast.LENGTH_LONG)
                            .show()
                        Log.i(
                            TAG,
                            "signIn failed: " + it.message
                        )
                        if(it.message == " code: 5 message: already sign in a user"){
                            signInBtn.isEnabled = false;
                            signOutBtn.isEnabled = true;
                        }else {
                            signOutBtn.isEnabled = false;
                        }
                    }

                //validateIdToken(huaweiAccount.idToken)
            } else {
                Toast.makeText(
                    this,
                    "HwID signIn failed: " + authHuaweiIdTask.exception.message,
                    Toast.LENGTH_LONG
                ).show()
                Log.i(
                    TAG,
                    "signIn failed: " + (authHuaweiIdTask.exception as ApiException).statusCode
                )

                signOutBtn.isEnabled = false;
            }
        }
    }

    override fun onDestroy() {
        signOut()
        super.onDestroy()
    }

    private fun getToken(){
        val context: Context = applicationContext
        PushService().getToken(context)
    }



    private fun signIn() {
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .setAccessToken()
            .createParams()
        val service = HuaweiIdAuthManager.getService(this, authParams)
        startActivityForResult(service.signInIntent, HUAWEIID_SIGNIN)
    }

    private fun initView(){
        signInBtn.setOnClickListener{
            signIn()
        }
        signOutBtn.setOnClickListener{
            signOut()
        }
    }

    private fun signOut() {
        AGConnectAuth.getInstance().signOut()
        Toast.makeText(
            this,
            "signOut Success",
            Toast.LENGTH_LONG
        ).show()
        Log.i(
            TAG,
            "signOut Success"
        )
        signInBtn.isEnabled = true;
        signOutBtn.isEnabled = false;
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
