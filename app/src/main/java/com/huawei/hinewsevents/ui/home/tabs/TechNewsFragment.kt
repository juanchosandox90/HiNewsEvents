package com.huawei.hinewsevents.ui.home.tabs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.viewmodel.TechNewsViewModel
import com.huawei.hinewsevents.utils.extension.Utils

class TechNewsFragment : Fragment() {

    val TAG: String = TechNewsFragment::class.simpleName.toString()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivError: ImageView
    private lateinit var progress: ProgressBar

    private var techNewsViewModel: TechNewsViewModel? = null
    private var newsListAdapter: TechNewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tech_news, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        ivError = view.findViewById(R.id.iv_error)
        progress = view.findViewById(R.id.progressBar)

        swipeRefreshLayout.setOnRefreshListener {
            if( Utils.haveNetworkConnection(view.context) ) {
                swipeRefreshLayout.isRefreshing = true
                getViewModelAndSetAdapter(view)
            }else{
                Utils.showToastMessage(view.context,"Need Network Connection!")
                showErrorLayout()
            }
        }

        setInitRecyclerView(view)

        techNewsViewModel = ViewModelProvider(this).get(TechNewsViewModel::class.java)

        // first fetch
        if( Utils.haveNetworkConnection(view.context) ) {
            getViewModelAndSetAdapter(view)
        }else{
            Utils.showToastMessage(view.context,"Need Network Connection!")
            showErrorLayout()
        }

        return view
    }

    private fun setInitRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        //use this setting to improve performance if you know that changes
        //in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }

    private fun getViewModelAndSetAdapter(view: View) {
        Log.i(TAG, "getViewModelAndSetAdapter")
        initAdapter()
        initState()
    }

    private fun initAdapter() {
        Log.i(TAG, "initAdapter")
        newsListAdapter = TechNewsAdapter { techNewsViewModel?.retry() }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = newsListAdapter

        techNewsViewModel?.newsList?.observe(viewLifecycleOwner, Observer { newsListAdapter!!.submitList(it)})

        techNewsViewModel?.newsList?.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "initAdapter : techNewsViewModel?.newsList?.observe")
            if(techNewsViewModel!!.listIsEmpty()){
                Log.d(TAG, "initState techNewsViewModel!!.listIsEmpty : showErrorLayout")
                showErrorLayout()
            }else{
                Log.d(TAG, "initState techNewsViewModel!!.listIsEmpty : nofityItemChange and hideErrorLayout")
                newsListAdapter?.nofityItemChange()
                hideErrorLayout()
            }
            newsListAdapter!!.submitList(it)
        })
    }

    private fun initState() {
        Log.d(TAG, "initState")
        techNewsViewModel?.getState()?.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                Log.d(TAG, "initState true : showProgress")
                showProgress()
            }else{
                Log.d(TAG, "initState false : hideProgress")
                hideProgress()
            }

        })
    }

    private fun showProgress(){
        progress.visibility = View.VISIBLE
    }
    private fun hideProgress(){
        progress.visibility = View.GONE
        // if swiped
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorLayout(){
        ivError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hideProgress()
    }

    private fun hideErrorLayout(){
        ivError.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


    class TechNewsAdapter(private val retry: () -> Unit) :
        PagedListAdapter<Article, TechNewsAdapter.ViewHolder>(NewsDiffCallback) {

        val TAG: String = TechNewsAdapter::class.simpleName.toString()

        class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

        fun nofityItemChange(){
            notifyItemChanged(super.getItemCount())
        }

        companion object {
            val NewsDiffCallback = object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                    return oldItem == newItem
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // create a new view
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_list_item, parent, false)

            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return super.getItemCount()
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {

            var articleItem : Article = getItem(position)!!
            //Log.d(TAG, "articleItem     :${articleItem.toString()}")

            holder.item.findViewById<TextView>(R.id.item_date_time).text = articleItem.published_date

            holder.item.findViewById<TextView>(R.id.item_title).text = articleItem.title

            holder.item.findViewById<TextView>(R.id.item_detail).text = articleItem.summary

            var imageUri: String = Uri.parse(
                "android.resource://" +
                        holder.item.context.packageName + "/" +
                        R.drawable.notfound.toString()
            ).toString()

            //Log.d(TAG, "articleItem.media :${articleItem.media}")
            if( articleItem.media != null ){
                imageUri = Uri.parse(articleItem.media).toString()
            }

            Utils.loadAndSetImageWithGlide(
                holder.item.context,
                holder.item.findViewById<ImageView>(R.id.item_image),
                imageUri
            )

            holder.item.findViewById<TextView>(R.id.item_rating).text = articleItem.rank.toString()


            holder.item.setOnClickListener {

                Log.d(TAG, "link     :${articleItem.link}")
                Log.d(TAG, "id       :${articleItem.id}")
                Log.d(TAG, "rating   :${articleItem.rank}")
                Log.d(TAG, "category :${articleItem.topic}")
                Log.d(TAG, "dateTime :${articleItem.published_date}")
                Log.d(TAG, "title    :${articleItem.title}")
                Log.d(TAG, "contents :${articleItem.summary}")
                Log.d(TAG, "imageUri :${articleItem.media}")

                Log.d(TAG,"onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                        " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}" )
                // TODO set and edit bundle content
                val bundle = bundleOf(
                    "link" to articleItem.link,
                    "rating" to articleItem.rank,
                    "category" to articleItem.topic,
                    "dateTime" to articleItem.published_date,
                    "title" to articleItem.title,
                    "contents" to articleItem.summary,
                    "imageUri" to articleItem.media
                )

                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle )
                //holder.item.findNavController().navigate( R.id.action_navigation_home_to_homeDetailFragment )

            }

        }

    }
}