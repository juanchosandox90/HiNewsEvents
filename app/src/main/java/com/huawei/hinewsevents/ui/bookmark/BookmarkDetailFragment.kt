package com.huawei.hinewsevents.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huawei.hinewsevents.R

class BookmarkDetailFragment : Fragment() {

    val TAG:String = "BookmarkDtlFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"2 onCreate")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark_detail, container, false)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG,"1 onAttach")
        super.onAttach(context)
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