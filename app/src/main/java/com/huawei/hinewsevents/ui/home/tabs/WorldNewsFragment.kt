package com.huawei.hinewsevents.ui.home.tabs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
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
import com.huawei.hinewsevents.data.viewmodel.WorldNewsViewModel
import com.huawei.hinewsevents.utils.extension.Utils

class WorldNewsFragment : Fragment() {

    val TAG: String = WorldNewsFragment::class.simpleName.toString()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivError: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var btnRetry: Button

    private var newsViewModel: WorldNewsViewModel? = null
    private var newsListAdapter: WorldNewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_world_news, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        ivError = view.findViewById(R.id.iv_error)
        progress = view.findViewById(R.id.progressBar)
        btnRetry = view.findViewById(R.id.btn_retry)
        btnRetry.setOnClickListener {
            Log.i(TAG, "onCreateView : btnRetry.setOnClick")
            // for refresh when newsViewModel listIsEmpty() state
            newsViewModel = ViewModelProvider(this).get(WorldNewsViewModel::class.java)
            getViewModelAndSetAdapter(view)
        }

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

        newsViewModel = ViewModelProvider(this).get(WorldNewsViewModel::class.java)

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
        newsListAdapter = WorldNewsAdapter { newsViewModel?.retry() }
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = newsListAdapter

        newsViewModel?.newsList?.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "initAdapter : worldNewsViewModel?.newsList?.observe")
            if(newsViewModel!!.listIsEmpty()){
                Log.d(TAG, "initState worldNewsViewModel!!.listIsEmpty : showErrorLayout : errorMsg : " + newsViewModel?.getErrorMsg())
                showErrorLayout()
                Utils.showToastMessage(context,"Could Not Get Data From API!\nSometimes HTTP 429 Too Many Requests")
            }else{
                Log.d(TAG, "initState worldNewsViewModel!!.listIsEmpty : nofityItemChange and hideErrorLayout")
                newsListAdapter?.nofityItemChange()
                hideErrorLayout()
            }
            newsListAdapter!!.submitList(it)
        })
    }

    private fun initState() {
        Log.d(TAG, "initState")
        newsViewModel?.getState()?.observe(viewLifecycleOwner, Observer { state ->
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
        btnRetry.visibility = View.VISIBLE
        ivError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hideProgress()
    }

    private fun hideErrorLayout(){
        btnRetry.visibility = View.GONE
        ivError.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


    class WorldNewsAdapter(private val retry: () -> Unit) :
        PagedListAdapter<Article, WorldNewsAdapter.ViewHolder>(NewsDiffCallback) {

        val TAG: String = WorldNewsAdapter::class.simpleName.toString()

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

            holder.item.findViewById<TextView>(R.id.item_rating).text = articleItem.rank.toString()

            var imageUri: String =
                Utils.getDefaultImageUri(holder.item.context, R.drawable.notfound.toString())

            //Log.d(TAG, "articleItem.media :${articleItem.media}")
            if( articleItem.media != null ){
                imageUri = Uri.parse(articleItem.media).toString()
            }

            Utils.loadAndSetImageWithGlide(
                holder.item.context,
                holder.item.findViewById<ImageView>(R.id.item_image),
                imageUri
            )

            holder.item.setOnClickListener {
                val bundle = bundleOf( "article" to articleItem)
                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle )
            }

        }

    }

}