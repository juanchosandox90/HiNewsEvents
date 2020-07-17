package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.huawei.hinewsevents.R


class HomeFragmentListDemo : Fragment() {

    val TAG: String = "HomeFragmentList"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home_list, container, false)

        //setAdapterPersonList(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            Log.d(  TAG, "onViewCreated getInt(ARG_OBJECT).toString() ${getInt(ARG_OBJECT).toString()}"
            )
            setAdapterPersonList(view)
        }
    }

    // To Test view
    fun setAdapterPersonList(view: View) {
        var randomNumb = (4..15).random()
        Log.d(TAG, "setAdapterPersonList with randomNumb $randomNumb")
        val viewAdapter = MyAdapter(Array(randomNumb) { "Person ${it + 1}" })
        view.findViewById<RecyclerView>(R.id.leaderboard_list).run {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    companion object {
        private const val ARG_OBJECT = "object"
    }


    open class HomeFragmentListDemoCollectionAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            val fragment = HomeFragmentListDemo()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }


    ///
    // To Test view
    class MyAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // create a new view
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_list_item, parent, false)


            return ViewHolder(itemView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.item.findViewById<TextView>(R.id.user_name_text).text = myDataset[position]

            holder.item.findViewById<ImageView>(R.id.user_avatar_image)
                .setImageResource(listOfAvatars[position % listOfAvatars.size])

            holder.item.setOnClickListener {
                val bundle = bundleOf(USERNAME_KEY to myDataset[position])

                Log.d("Home", "$USERNAME_KEY : ${myDataset[position]}")

                val toast = Toast.makeText(
                    holder.itemView.context,
                    "$USERNAME_KEY : ${myDataset[position]}",
                    Toast.LENGTH_SHORT
                )
                toast.show()

                Log.d("HomeFragmentAdapter","onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                            " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}" )

                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment)
                //holder.item.findNavController().navigate( R.id.action_navigation_home_to_homeDetailFragment )

            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size

        companion object {
            const val USERNAME_KEY = "userName"
        }

        private val listOfAvatars = listOf(
            R.drawable.avatar_1_raster,
            R.drawable.avatar_2_raster,
            R.drawable.avatar_3_raster,
            R.drawable.avatar_4_raster,
            R.drawable.avatar_5_raster,
            R.drawable.avatar_6_raster
        )

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