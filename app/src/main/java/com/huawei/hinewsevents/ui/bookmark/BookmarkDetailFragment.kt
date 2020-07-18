package com.huawei.hinewsevents.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huawei.hinewsevents.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkDetailFragment : Fragment() {

    val TAG:String = "BookmarkDtlFragment"

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        Log.d(TAG,"2 onCreate")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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