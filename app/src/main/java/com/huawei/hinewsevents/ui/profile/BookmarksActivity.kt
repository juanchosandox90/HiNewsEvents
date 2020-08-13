package com.huawei.hinewsevents.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hinewsevents.R

class BookmarksActivity : AppCompatActivity() {

    val TAG: String = BookmarksActivity::class.simpleName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Profile Bookmarks"

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(TAG, "onBackPressed")
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp")
        onBackPressed()
        return true
    }
}