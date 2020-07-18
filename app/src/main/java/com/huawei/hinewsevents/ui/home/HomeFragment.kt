package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hinewsevents.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment() {

    val TAG: String = "HomeFragment"

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var homeFragmentListDemoCollectionAdapter: HomeFragmentListDemo.HomeFragmentListDemoCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // TODO showing refresh fragment when onCreateView
        view.constraintLayout_home.setBackgroundColor(randomColorGenerator())

        viewPager = view.findViewById(R.id.viewpager)

        // Try to test sample list and tabLayout for viewpager
        homeFragmentListDemoCollectionAdapter =
            HomeFragmentListDemo.HomeFragmentListDemoCollectionAdapter(this)
        viewPager.adapter = homeFragmentListDemoCollectionAdapter

        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "TAB ${(position + 1)}"
        }.attach()
        // Try to test sample list and tab loyout for viewpager

        return view

    }

    // TODO remove after test
    private fun randomColorGenerator(): Int {
        return Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
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

