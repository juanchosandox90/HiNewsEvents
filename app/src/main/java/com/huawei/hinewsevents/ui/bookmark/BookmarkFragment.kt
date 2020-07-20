package com.huawei.hinewsevents.ui.bookmark

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.huawei.hinewsevents.R
import kotlinx.android.synthetic.main.fragment_bookmark.view.*
import java.util.*


class BookmarkFragment : Fragment() {

    val TAG:String = "BookmarkFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG,"3 onCreateView")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        // TODO showing refresh fragment when onCreateView
        view.constraintLayout_bookmark.setBackgroundColor( randomColorGenerator() )

        view.findViewById<Button>(R.id.btn_detail_bookmark).setOnClickListener {

            Log.d( TAG, "navigation_bookmark onCLick and findNavController().currentDestination ${findNavController().currentDestination} " +
                    "{${findNavController().currentDestination?.id} - navigation_bookmark ${R.id.navigation_bookmark}" )

            findNavController().navigate(R.id.action_navigation_bookmark_to_bookmarkDetailFragment)

        }

        return view
    }


    private fun randomColorGenerator(): Int {
        return Color.argb(255, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
    }


    override fun onAttach(context: Context) {
        Log.d(TAG,"1 onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"2 onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG,"4 onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG,"5 onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"6 onResume")
        super.onResume()
    }

    override fun onStop() {
        Log.d(TAG,"7 onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG,"8 onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG,"9 onDetach")
        super.onDetach()
    }

}
