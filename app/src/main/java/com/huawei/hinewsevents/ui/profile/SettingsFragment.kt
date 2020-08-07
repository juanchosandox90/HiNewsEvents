package com.huawei.hinewsevents.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.Utils

class SettingsFragment: Fragment() {
    val TAG: String = "SettingsFragment"
    private var loggedIn: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        view.findViewById<TextView>(R.id.item_font_size).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!")
        }
        view.findViewById<TextView>(R.id.item_font_size_value).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!")
        }
        view.findViewById<ImageView>(R.id.goDetailIcon1).setOnClickListener {
            Utils.showToastMessage(context, "Font Size Change Request!")
        }
        view.findViewById<TextView>(R.id.item_language).setOnClickListener {
            Utils.showToastMessage(context, "Language Change Request!")
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
            Utils.showToastMessage(context, "Clear Cash Request!")
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
        return view
    }
    private fun signOut() {
        AGConnectAuth.getInstance().signOut()
        Utils.showToastMessage(context, "SignOut Success")
        Log.i(TAG,"signOut Success")
        //Utils.loadAndSetImageWithGlide( context, profileImage, failedImageUri );
        loggedIn = false
        //loginLogOut.text = "Login with HuaweiID"
        //nameSurname.text = "Name Surname"
    }
}