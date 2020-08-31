package com.huawei.hinewsevents.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.Utils
import com.huawei.hinewsevents.data.services.push.PushService
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import java.util.*


class ProfileFragment : Fragment() {

    val TAG: String = "ProfileFragment"

    private val HUAWEIID_SIGNIN = 1002

    private var loggedIn: Boolean = false
    private lateinit var profileImage:ImageView
    private lateinit var nameSurname:TextView
    private lateinit var loginLogOut:TextView

    private lateinit var turnOnOfNotification: SwitchCompat

    private lateinit var failedImageUri:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // TODO showing refresh fragment when onCreateView
        //view.constraintLayout_profile.setBackgroundColor(randomColorGenerator())

        profileImage = view.findViewById(R.id.imageView_profile)
        nameSurname = view.findViewById(R.id.tv_profile_nameSurname)
        loginLogOut = view.findViewById(R.id.tv_profile_loginLogOut)
        turnOnOfNotification = view.findViewById(R.id.switch_profile_notification_onoff)
        turnOnOfNotification.setOnClickListener{
            if (turnOnOfNotification.isChecked()){
                PushService(it.context).enableNotifications()
            }else {
                PushService(it.context).disableNotifications()
            }
        }

        failedImageUri =
            Utils.getDefaultImageUri(profileImage.context, R.drawable.icon_account.toString())

        if( AGConnectAuth.getInstance().currentUser != null ){
            Log.i(TAG,"currentUser.. ${AGConnectAuth.getInstance().currentUser}")
            loggedIn = true;
            loginLogOut.text = "Log Out"
            nameSurname.text = AGConnectAuth.getInstance().currentUser.displayName
            Utils.loadAndSetImageWithGlide( context, profileImage,  AGConnectAuth.getInstance().currentUser.photoUrl );
        }else{
            Log.i(TAG,"no currentUser.. ")
            silentSignIn()
        }

        view.findViewById<CardView>(R.id.cv_image).setOnClickListener {
            if (loggedIn) {
                signOut();
            } else {
                silentSignIn();
            }
        }

        //view.findViewById<Button>(R.id.btn_detail_profile).setOnClickListener {

        //    Log.d( TAG, "btn_detail_profile onCLick and findNavController().currentDestination ${findNavController().currentDestination} " +
        //                "{${findNavController().currentDestination?.id} - navigation_profile ${R.id.navigation_profile}" )

        //    val bundle = Bundle()
        //    bundle.putString("username", "blahUser")
        //    bundle.putString("password", "blahPass")
        //    bundle.putString("fullName", "blah blah user")
        //    findNavController().navigate( R.id.action_navigation_profile_to_profileDetailFragment, bundle )

        //}

        return view
    }

    private fun silentSignIn() {
        if(context?.let { Utils.haveNetworkConnection(it) }!!){
            Log.i(TAG,"silentSignIn..")
            val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams()
            val service = HuaweiIdAuthManager.getService(context, authParams)
            startActivityForResult(service.signInIntent, HUAWEIID_SIGNIN)
        }else{
            Utils.showToastMessage(context, "You need a network connection to SignIn");
        }
    }

    private fun signOut() {
        AGConnectAuth.getInstance().signOut()
        Utils.showToastMessage(context, "SignOut Success");
        Log.i(TAG,"signOut Success")
        Utils.loadAndSetImageWithGlide( context, profileImage, failedImageUri );
        loggedIn = false;
        loginLogOut.text = "Login with HuaweiID"
        nameSurname.text = "Name Surname"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HUAWEIID_SIGNIN) {
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
                        Utils.showToastMessage(context, "signIn success. Welcome ${user.displayName}");

                        Log.i(TAG, "signIn success. Huawei Account Details: $huaweiAccount")
                        Log.d(TAG,"AccessToken: " + huaweiAccount.accessToken)
                        Log.d(TAG,"IDToken: " + huaweiAccount.idToken)

                        loggedIn = true;
                        Utils.loadAndSetImageWithGlide( context, profileImage, user.photoUrl );
                        loginLogOut.text = "Log Out"
                        nameSurname.text = user.displayName

                    }.addOnFailureListener {
                        //Utils.showToastMessage(context, "HwID signIn failed: ${it.message}");
                        Log.e(TAG, "signIn failed: " + it.message)

                        if (it.message == " code: 5 message: already sign in a user") {
                            loggedIn = true;
                            loginLogOut.text = "Log Out"
                            nameSurname.text = AGConnectAuth.getInstance().currentUser.displayName
                            Utils.loadAndSetImageWithGlide( context, profileImage,  AGConnectAuth.getInstance().currentUser.photoUrl );
                        }else{
                            loggedIn = false;
                            loginLogOut.text = "Login with HuaweiID"
                            nameSurname.text = "Name Surname"
                            Utils.loadAndSetImageWithGlide( context, profileImage, failedImageUri );
                        }
                    }
            } else {
                Utils.showToastMessage(context, "HwID signIn failed: ${authHuaweiIdTask.exception.message}");
                Log.e(TAG,"signIn failed: " + (authHuaweiIdTask.exception as ApiException).statusCode +" - " + authHuaweiIdTask.exception.message )
                Utils.loadAndSetImageWithGlide( context, profileImage, failedImageUri );
                loggedIn = false;
                loginLogOut.text = "Login with HuaweiID"
                nameSurname.text = "Name Surname"
            }
        }

    }





    private fun randomColorGenerator(): Int {
        return Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
    }


    override fun onAttach(context: Context) {
        Log.d(TAG, "1 onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "2 onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "4 onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "5 onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "6 onResume")
        super.onResume()
    }

    override fun onStop() {
        Log.d(TAG, "7 onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "8 onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "9 onDetach")
        super.onDetach()
    }

}
