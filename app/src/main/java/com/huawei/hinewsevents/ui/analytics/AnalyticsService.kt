package com.huawei.hinewsevents.ui.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools

class AnalyticsService{
    lateinit var instance : HiAnalyticsInstance
    private val TAG = "AnalyticsService"

    private fun init(context: Context) {
        HiAnalyticsTools.enableLog()
        instance = HiAnalytics.getInstance(context);
        Log.i(TAG, "init: Analytics Service is ON")
    }

    fun signStatusAnalytics(context: Context, isSignIn: Boolean) {
        init(context)
        var bundle = Bundle()
        var signStatus = if(isSignIn) "Signed in" else "Signed out"
        bundle.putString("SignInStatus", signStatus)
        instance.onEvent("SignStatus", bundle)
        Log.i(TAG, "signStatusAnalytics: Analytics SignStatus is pushed!")
    }
}