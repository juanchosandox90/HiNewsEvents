package com.huawei.hinewsevents.ui.push

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessageService
import java.lang.Exception

class PushService : HmsMessageService(){

    private val TAG : String = "[PushService]"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.i(TAG, "Receive token $p0")
        if (p0 != null) {
             onTokenChanged(p0)
        }
    }

    fun getToken(context: Context) {
        Thread() {
            kotlin.run {
                try {
                    val appId: String = AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                    val pushtoken: String = HmsInstanceId.getInstance(context).getToken(appId, "HCM")

                    if (!TextUtils.isEmpty(pushtoken)){
                        Log.i(TAG, "get token $pushtoken")
                        onTokenChanged(pushtoken)
                    }else {
                        Log.i(TAG, "get token is null pushtoken = $pushtoken")
                    }
                }catch (e: Exception){
                    Log.i(TAG, "getToken failed $e")
                }
            }
        }.start()
    }

    fun onTokenChanged(token: String) {
        Log.i(TAG, "Token is: $token")
    }
}