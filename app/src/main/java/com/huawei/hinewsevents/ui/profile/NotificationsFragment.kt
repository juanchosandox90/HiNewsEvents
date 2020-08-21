package com.huawei.hinewsevents.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.services.push.PushService
import com.huawei.hinewsevents.utils.extension.Utils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnNotifyImp: TextView
    private lateinit var btnNotifyDaily: TextView
    private lateinit var btnNotifyAll: TextView
    private lateinit var turnOnOfNotification: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile_notifications, container, false)
        btnNotifyImp = view.findViewById(R.id.notiffreq_button_imp)
        btnNotifyDaily = view.findViewById(R.id.notiffreq_button_daily)
        btnNotifyAll = view.findViewById(R.id.notiffreq_button_everthing)
        turnOnOfNotification = view.findViewById(R.id.switch_profile_notification_onoff)

        turnOnOfNotification.setOnClickListener {
            if (turnOnOfNotification.isChecked) {
                PushService(it.context).enableNotifications()
            } else {
                PushService(it.context).disableNotifications()
            }
        }

        view.findViewById<TextView>(R.id.notiffreq_button_imp).setOnClickListener {
            btnNotifyImp.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_sq))
            btnNotifyDaily.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            btnNotifyAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            Utils.showToastMessage(context, "Push Frequency Set to 1!")
        }
        view.findViewById<TextView>(R.id.notiffreq_button_daily).setOnClickListener {
            btnNotifyImp.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            btnNotifyDaily.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_sq))
            btnNotifyAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            Utils.showToastMessage(context, "Push Frequency Set to 2!")
        }
        view.findViewById<TextView>(R.id.notiffreq_button_everthing).setOnClickListener {
            btnNotifyImp.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            btnNotifyDaily.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_sq))
            btnNotifyAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_red_sq))
            Utils.showToastMessage(context, "Push Frequency Set to 3!")
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}