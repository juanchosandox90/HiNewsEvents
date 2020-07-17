package com.huawei.hinewsevents.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hinewsevents.R
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val HUAWEIID_SIGNIN = 1002
    private lateinit var signOutBtn : TextView
    private lateinit var signInBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            }
        }
    }

    override fun onDestroy() {
        signOut()
        super.onDestroy()
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

}
