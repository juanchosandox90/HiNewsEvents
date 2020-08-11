package com.huawei.hinewsevents.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.ui.analytics.AnalyticsService
import com.huawei.hinewsevents.ui.crash.CrashService
import com.huawei.hinewsevents.ui.push.PushService
import com.huawei.hinewsevents.utils.extension.Utils
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val HUAWEIID_SIGNIN = 1002
    private lateinit var signOutBtn: TextView
    private lateinit var signInBtn: TextView
    private lateinit var triggerCrashBtn: TextView
    private lateinit var stopCrashServiceBtn: TextView
    private lateinit var navController: NavController
    private var currentNavController: LiveData<NavController>? = null

    private lateinit var bottomNavView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getToken()

        if (savedInstanceState == null) {
            setupBottomNavigationBarAndController()
        } // Else, need to wait for onRestoreInstanceState

        signInBtn = findViewById(R.id.button_signin)
        signOutBtn = findViewById(R.id.button_signout)
        //Crash Service Test Buttons
        triggerCrashBtn = findViewById(R.id.button_trigger_crash);
        stopCrashServiceBtn = findViewById(R.id.button_stop_crashService);
        initView()
        if( AGConnectAuth.getInstance().currentUser != null ){
            val user = AGConnectAuth.getInstance().currentUser
            Log.i(TAG, "currentUser Huawei Account Details: $user")
            Utils.showToastMessage(this, "Welcome ${user.displayName}");
            signInBtn.isEnabled = false;
            signOutBtn.isEnabled = true;

            startSignAnalytics(true)
            startCrashService()
        }else{
            Log.i(TAG,"no currentUser.. ")
            signIn()
        }
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
                        Utils.showToastMessage(this, "signIn success. Welcome ${user.displayName}");
                        Log.i(TAG, "signIn success. Huawei Account Details: $huaweiAccount")
                        Log.d(TAG,"AccessToken: " + huaweiAccount.accessToken)
                        Log.d(TAG,"IDToken: " + huaweiAccount.idToken)
                        signInBtn.isEnabled = false;
                        signOutBtn.isEnabled = true;

                        startSignAnalytics(true)

                    }.addOnFailureListener {
                        Utils.showToastMessage(this, "HwID signIn failed: ${it.message}");
                        Log.e(TAG, "signIn failed: " + it.message)
                        if (it.message == " code: 5 message: already sign in a user") {
                            signInBtn.isEnabled = false;
                            signOutBtn.isEnabled = true;
                            startSignAnalytics(true)
                        } else {
                            signOutBtn.isEnabled = false;
                            startSignAnalytics(false)
                        }
                    }

                //validateIdToken(huaweiAccount.idToken)
            } else {
                Utils.showToastMessage(this, "HwID signIn failed: ${authHuaweiIdTask.exception.message}");
                Log.e(TAG,"signIn failed: " + (authHuaweiIdTask.exception as ApiException).statusCode
                )

                signOutBtn.isEnabled = false;
            }
        }
    }

    override fun onDestroy() {
        signOut()
        super.onDestroy()
    }

    private fun getToken() {
        val context: Context = applicationContext
        PushService(context).getToken()
    }

    private fun startSignAnalytics(isSignIn: Boolean) {
        val context: Context = applicationContext
        AnalyticsService().signStatusAnalytics(context, isSignIn)
    }

    private fun startCrashService(){
        CrashService().enableAgCrashService();
    }

    private fun stopCrashService(){
        CrashService().disableAgCrashService();
    }

    private fun signIn() {
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .setAccessToken()
            .createParams()
        val service = HuaweiIdAuthManager.getService(this, authParams)
        startActivityForResult(service.signInIntent, HUAWEIID_SIGNIN)
    }

    private fun initView() {
        signInBtn.setOnClickListener {
            signIn()
            startSignAnalytics(true)
        }
        signOutBtn.setOnClickListener {
            signOut()
            startSignAnalytics(false)
        }
        triggerCrashBtn.setOnClickListener {
            CrashService().testIt()
        }
        stopCrashServiceBtn.setOnClickListener {
            stopCrashService();
        }
    }

    private fun signOut() {
        AGConnectAuth.getInstance().signOut()
        Utils.showToastMessage(this, "signOut Success");
        Log.i(TAG,"signOut Success"
        )
        signInBtn.isEnabled = true;
        signOutBtn.isEnabled = false;
        startSignAnalytics(false)
    }
    //We should add Bottom Navigation Bar Menu structure into this activity.

    private fun showBottomNav() {
        bottomNavView.visibility = View.VISIBLE
        supportActionBar?.show()
    }

    private fun hideBottomNav() {
        bottomNavView.visibility = View.GONE
    }
    // for webView Fragment
    private fun hideBottomNavAndActionBar() {
        bottomNavView.visibility = View.GONE
        supportActionBar?.hide()
    }
    private fun setupBottomNavigationBarAndController() {

        bottomNavView = findViewById(R.id.bottom_nav_view)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.navigateUp()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> showBottomNav()
                R.id.navigation_profile -> showBottomNav()
                R.id.navigation_bookmark -> showBottomNav()
                R.id.webViewFragment -> hideBottomNavAndActionBar()
                else -> hideBottomNav()
            }
        }

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


}

