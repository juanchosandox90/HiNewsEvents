package com.huawei.hinewsevents.data.services.push

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.HmsMessaging
import java.lang.Exception

class PushService constructor(): HmsMessageService(){

    private val TAG : String = "[PushService]"
    var pushtoken = ""
    private lateinit var appContext: Context
    private lateinit var instance : HmsMessaging

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.i(TAG, "Receive token $p0")
        if (p0 != null) {
             onTokenChanged(p0)
        }
    }

    constructor(context: Context) : this() {
        appContext = context
        instance = HmsMessaging.getInstance(context)
    }

    fun getToken() {
        Thread() {
            kotlin.run {
                try {
                    val appId: String = AGConnectServicesConfig.fromContext(appContext).getString("client/app_id")
                    pushtoken = HmsInstanceId.getInstance(appContext).getToken(appId, "HCM")

                    if (!TextUtils.isEmpty(pushtoken)){
                        Log.i(TAG, "get token $pushtoken")
                        onTokenChanged(pushtoken)
                    }else {
                        Log.i(TAG, "get token is null pushtoken = $pushtoken")
                    }
                }catch (e: Exception){
                    Log.i(TAG, "getToken failed ${e.message}")
                }
            }
        }.start()
    }

    fun onTokenChanged(token: String) {
        Log.i(TAG, "Token is: $token")
    }

    fun unsubscribeGivenTopic(topic : String){
        val task = instance.unsubscribe(topic)
        Log.i(TAG, "Trying to unsubscribe $topic ...")
        task.addOnSuccessListener{
            Log.i(TAG, "unsubscribeGivenTopic: SUCCESSFUL")
        }
        task.addOnFailureListener{
            Log.i(TAG, "unsubscribeGivenTopic: Error: ${it.message}")
        }



    }

    fun subscribeGivenTopic(topic: String){
        val task = instance.subscribe(topic)
        Log.i(TAG, "Trying to subscribe $topic ...")
        task.addOnSuccessListener{
            Log.i(TAG, "subscribeGivenTopic: SUCCESSFUL")
        }
        task.addOnFailureListener{
            Log.i(TAG, "subscribeGivenTopic: Error: ${it.message}")
        }
    }

    fun disableNotifications(){
        val task = instance.turnOffPush()
        Log.i(TAG, "Trying to disable notifications ...")
        task.addOnSuccessListener{
            Log.i(TAG, "disableNotifications: SUCCESSFUL")
            Toast.makeText(appContext, "Notifications are disabled.", Toast.LENGTH_SHORT).show()
        }
        task.addOnFailureListener{
            Log.i(TAG, "disableNotifications: Error: ${it.message}")
            Toast.makeText(appContext, "Failed while disabling notifications.", Toast.LENGTH_SHORT).show()
        }
    }

    fun enableNotifications(){
        val task = instance.turnOnPush()
        Log.i(TAG, "Trying to unsubscribe enable notifications ...")
        task.addOnSuccessListener{
            Log.i(TAG, "enableNotifications: SUCCESSFUL")
            Toast.makeText(appContext, "Notifications are enabled.", Toast.LENGTH_SHORT).show()
        }
        task.addOnFailureListener{
            Log.i(TAG, "enableNotifications: Error: ${it.message}")
            Toast.makeText(appContext, "Failed while enabling notifications.", Toast.LENGTH_SHORT).show()
        }

    }
}