package com.huawei.hinewsevents.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.Utils
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

class SettingsFragment: Fragment() {
    val TAG: String = "SettingsFragment"
    private var loggedIn: Boolean = false

    private val requestCodeHuaweiSignIn = 1002

    private lateinit var btn_sigOut: TextView
    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        loggedIn = AGConnectAuth.getInstance().currentUser != null

        view.findViewById<TextView>(R.id.item_font_size).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!");
        }
        view.findViewById<TextView>(R.id.item_font_size_value).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon1).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!")
        }
        view.findViewById<TextView>(R.id.item_language).setOnClickListener {
            Utils.showToastMessage(context, "Language Change Request!");
        }
        view.findViewById<TextView>(R.id.item_language_value).setOnClickListener {
            Utils.showToastMessage(context, "Language Change Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon2).setOnClickListener {
            Utils.showToastMessage(context, "Language Change Request!")
        }
        view.findViewById<TextView>(R.id.item_appUsage).setOnClickListener {
            Utils.showToastMessage(context, "App Usage Change Request!")
        }
        view.findViewById<TextView>(R.id.item_appUsage_value).setOnClickListener {
            Utils.showToastMessage(context, "App Usage Change Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon3).setOnClickListener {
            Utils.showToastMessage(context, "App Usage Change Request!")
        }
        view.findViewById<TextView>(R.id.item_feedback).setOnClickListener {
            Utils.showToastMessage(context, "Leave Feeback  Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon4).setOnClickListener {
            Utils.showToastMessage(context, "Leave Feeback  Request!")
        }
        view.findViewById<TextView>(R.id.item_clearCache).setOnClickListener {
            Utils.showToastMessage(context, "Clear Cash Request!");
        }
        view.findViewById<TextView>(R.id.item_clearCache_value).setOnClickListener {
            Utils.showToastMessage(context, "Clear Cash Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon5).setOnClickListener {
            Utils.showToastMessage(context, "Clear Cash Request!")
        }
        view.findViewById<TextView>(R.id.item_inviteFriends).setOnClickListener {
            Utils.showToastMessage(context, "Invite Friends Request!")
        }
        view.findViewById<TextView>(R.id.item_inviteFriends).setOnClickListener {
            Utils.showToastMessage(context, "Invite Friends Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon6).setOnClickListener {
            Utils.showToastMessage(context, "Invite Friends Request!")
        }
        view.findViewById<TextView>(R.id.item_privacyStatement).setOnClickListener {
            Utils.showToastMessage(context, "Privacy Statement Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon7).setOnClickListener {
            Utils.showToastMessage(context, "Privacy Statement Request!")
        }
        view.findViewById<TextView>(R.id.item_terms).setOnClickListener {
            Utils.showToastMessage(context, "Terms of Service Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon8).setOnClickListener {
            Utils.showToastMessage(context, "Terms of Service Request!")
        }
        view.findViewById<TextView>(R.id.item_licenses).setOnClickListener {
            Utils.showToastMessage(context, "Third Party Licenses Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon9).setOnClickListener {
            Utils.showToastMessage(context, "Third Party Licenses Request!")
        }
        view.findViewById<TextView>(R.id.item_aboutUs).setOnClickListener {
            Utils.showToastMessage(context, "About Us Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon10).setOnClickListener {
            Utils.showToastMessage(context, "About Us Request!")
        }
        view.findViewById<TextView>(R.id.item_rateUs).setOnClickListener {
            Utils.showToastMessage(context, "Rate Us Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon11).setOnClickListener {
            Utils.showToastMessage(context, "Rate Us Request!")
        }
        view.findViewById<TextView>(R.id.logOut_button).setOnClickListener {
            signOut()
        }

        setSignStatusAndViews(loggedIn)

        return view
    }

    private fun signOut() {
        AGConnectAuth.getInstance().signOut()
        Utils.showToastMessage(context, "SignOut Success");
        Log.i(TAG,"signOut Success")
        setSignStatusAndViews(false)
        menuItemVisibility(true)
    }

    private fun silentSignIn() {
        if(context?.let { Utils.haveNetworkConnection(it) }!!){
            Log.i(TAG,"silentSignIn..")
            val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams()
            val service = HuaweiIdAuthManager.getService(context, authParams)
            startActivityForResult(service.signInIntent, requestCodeHuaweiSignIn)
        }else{
            Utils.showToastMessage(context, "You need a network connection to SignIn")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeHuaweiSignIn) {
            //login success
            //get user message by parseAuthResultFromIntent
            val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful) {
                val huaweiAccount = authHuaweiIdTask.result
                val accessToken = huaweiAccount.accessToken
                val credential = HwIdAuthProvider.credentialWithToken(accessToken)
                AGConnectAuth.getInstance().signIn(credential)
                    .addOnSuccessListener { signInResult -> // onSuccess
                        val user = signInResult.user
                        Utils.showToastMessage(context, "signIn success. Welcome ${user.displayName}")

                        //Log.i(TAG, "signIn success. Huawei Account Details: $huaweiAccount")
                        //Log.d(TAG,"AccessToken: " + huaweiAccount.accessToken)
                        //Log.d(TAG,"IDToken: " + huaweiAccount.idToken)

                        setSignStatusAndViews(true)
                        menuItemVisibility(false)

                    }.addOnFailureListener {
                        Log.e(TAG, "signIn failed: " + it.message)
                        if (it.message == " code: 5 message: already sign in a user") {
                            setSignStatusAndViews(true)
                            menuItemVisibility(false)
                        }else{
                            Utils.showToastMessage(context, "HwID signIn failed: ${ it.message}")
                            setSignStatusAndViews(false)
                            menuItemVisibility(true)
                        }
                    }
            } else {
                Utils.showToastMessage(context, "HwID signIn failed: ${authHuaweiIdTask.exception.message}")
                Log.e(TAG,"signIn failed: " + (authHuaweiIdTask.exception as ApiException).statusCode +" - " + authHuaweiIdTask.exception.message )
                setSignStatusAndViews(false)
            }
        }

    }

    private fun setSignStatusAndViews(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            loggedIn = true
            btn_sigOut.isEnabled = true
            btn_sigOut.setBackgroundResource(R.drawable.bg_red_sq)
        } else {
            loggedIn = false
            btn_sigOut.isEnabled = false
            btn_sigOut.setBackgroundResource(R.drawable.bg_gray_sq)

        }
    }

    private fun menuItemVisibility(it: Boolean){
        menu.findItem(R.id.action_login).isVisible = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "2 onCreate")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_settings, menu)
        Log.i(TAG,"onCreateOptionsMenu")
        if( loggedIn  ){
            Log.i(TAG,"currentUser.. ${AGConnectAuth.getInstance().currentUser}")
            menu.findItem(R.id.action_login).isVisible = false
        }else{
            Log.i(TAG,"no currentUser.. ")
            menu.findItem(R.id.action_login).isVisible = true
        }
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG,"onOptionsItemSelected")
        val id = item.itemId
        if (id == R.id.action_login){
            Log.i(TAG,"onOptionsItemSelected : action_login")
            silentSignIn()
        }
        return super.onOptionsItemSelected(item)
    }

}