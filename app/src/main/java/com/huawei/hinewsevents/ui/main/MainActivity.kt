package com.huawei.hinewsevents.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.ui.push.PushService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getToken()
    }

    private fun getToken(){
        val context: Context = applicationContext
        PushService().getToken(context)
    }



    //We should add Bottom Navigation Bar Menu structure into this activity.
}
