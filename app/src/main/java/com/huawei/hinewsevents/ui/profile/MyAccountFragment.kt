package com.huawei.hinewsevents.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hinewsevents.R

class MyAccountFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile_threetab, container, false)

        viewPager2 = view.findViewById(R.id.pager)
        viewPager2.adapter = MyAccountPagerAdapter(this)

        tabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager2, TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
            when (position){
                0 -> tab.text = "Kütüphane"
                1 -> tab.text = "Bildirimler"
                2 -> tab.text = "Ayarlar"
            }
        }).attach()

        // return super.onCreateView(inflater, container, savedInstanceState)
        return view
    }


    private class MyAccountPagerAdapter(myAccountFragment: MyAccountFragment) : FragmentStateAdapter(myAccountFragment) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position){
                0 -> LibraryFragment()
                1 -> NotificationsFragment()
                else -> ProfileFragment()
            }
        }

    }
}