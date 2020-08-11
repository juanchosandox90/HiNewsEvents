package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.ui.home.tabs.TabsAdapter


class HomeFragment : Fragment() {

    val TAG: String = HomeFragment::class.simpleName.toString()

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewPager = view.findViewById(R.id.viewpager)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)

        viewPager.adapter = TabsAdapter(
            (activity as FragmentActivity).supportFragmentManager,
            lifecycle
        )

        // TODO set tab contents
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "Tech News"
                    1 -> tab.text = "World News"
                    2 -> tab.text = "Main News"
                    3 -> tab.text = "Finance"
                    4 -> tab.text = "Business"
                    5 -> tab.text = "Entertainment"
                    6 -> tab.text = "Latest HeadLines"
                }
            }).attach()

        return view

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

