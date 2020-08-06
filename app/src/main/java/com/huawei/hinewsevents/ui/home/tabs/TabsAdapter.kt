package com.huawei.hinewsevents.ui.home.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.huawei.hinewsevents.ui.home.HomeFragmentListDemo

class TabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    // TODO set tab count
    private var tabCount: Int = 5

    override fun getItemCount(): Int {
        return tabCount
    }

    // TODO set tab contents
    // news, tech, world, finance, business, economics, entertainment, events
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TechNewsFragment()
            1 -> fragment = WorldNewsFragment()
            2 -> fragment = MainNewsFragment()
            3 -> fragment = LatestHeadLinesFragment()
            4 -> fragment = HomeFragmentListDemo()
        }
        return fragment!!
    }

}