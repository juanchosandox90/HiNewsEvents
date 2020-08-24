package com.huawei.hinewsevents.data.services.crash

import android.util.Log
import com.huawei.agconnect.crash.AGConnectCrash

private val TAG = "CrashService"

class CrashService {

    fun enableAgCrashService() {
        AGConnectCrash.getInstance().enableCrashCollection(true) // enable crash service
        Log.i(TAG, "enable: Crash Service is ON")
    }

    fun disableAgCrashService() {
        AGConnectCrash.getInstance().enableCrashCollection(false) // enable crash service
        Log.i(TAG, "disable: Crash Service is OFF")
    }

    fun testIt() {
        AGConnectCrash.getInstance().testIt(this);
    }
}

private fun AGConnectCrash.testIt(crashService: CrashService) {
    AGConnectCrash.getInstance().setUserId("crashTestAdminMannequinJoe");
    divideByZero()
    //triggerNullPointerExp()
}

private fun divideByZero() {
    val numberOne = 2;
    val numberTwo = 0;
    AGConnectCrash.getInstance().setCustomKey("zeroDivisionKey", 0);
    //custom crash log configuration
    AGConnectCrash.getInstance().log(Log.WARN, "Warning! attempt fails with crash:  Division by zero Arithmetic Exception");
    Log.i(TAG, "Division by zero  attempt " + numberOne / numberTwo)
}

private fun triggerNullPointerExp() {
    val sayi: IntArray? = null
    // Add a key-value pair of the string type.
    AGConnectCrash.getInstance().setCustomKey("nullPointerKey", "null");
    AGConnectCrash.getInstance().log(Log.WARN, "Warning! attempt fails with crash: Null pointer Exception");
    Log.i(TAG, "Reaching First Index of an Null Array attempt: " + sayi!![0])
}