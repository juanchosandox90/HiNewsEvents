package com.huawei.hinewsevents.ui.home.tabs

import android.view.LayoutInflater
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.viewmodel.LatestHeadLinesViewModel
import com.huawei.hinewsevents.data.viewmodel.MainNewsViewModel
import com.huawei.hinewsevents.utils.extension.Utils

class LatestHeadLinesFragment : Fragment() {

    val TAG: String = LatestHeadLinesFragment::class.simpleName.toString()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivError: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var btnRetry: Button

    private var newsViewModel: LatestHeadLinesViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_latest_head_lines, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        ivError = view.findViewById(R.id.iv_error)
        progress = view.findViewById(R.id.progressBar)
        btnRetry = view.findViewById(R.id.btn_retry)
        btnRetry.setOnClickListener {
            Log.i(TAG, "onCreateView : btnRetry.setOnClick")
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

        showProgress()

        newsViewModel = ViewModelProvider(this).get(LatestHeadLinesViewModel::class.java)
        newsViewModel!!.refreshData()
        newsViewModel!!.latestNewsListData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                showErrorLayout()
            } else {
                //Log.d(TAG, "it  :${it.toString()}")
                var status: String = it.status
                if( status != "ok"){
                    Utils.showToastMessage(context, status)
                }else{

                    recyclerView.adapter = LatestNewsAdapter(it.articles)

                    if( ivError.isVisible ) hideErrorLayout()

                    hideProgress()

                }
                swipeRefreshLayout.isRefreshing = false
            }
        })
        hideProgress()
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



    class LatestNewsAdapter(private val newsArticleList: List<Article>) :
        RecyclerView.Adapter<LatestNewsAdapter.ViewHolder>() {

        val TAG: String = "LatestLinesAdapter"

        class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

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
            return newsArticleList.size
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {

            holder.item.findViewById<TextView>(R.id.item_date_time).text = newsArticleList[position].published_date

            holder.item.findViewById<TextView>(R.id.item_title).text = newsArticleList[position].title

            holder.item.findViewById<TextView>(R.id.item_detail).text = newsArticleList[position].summary

            var imageUri: String =
                Utils.getDefaultImageUri(holder.item.context, R.drawable.notfound.toString())

            //Log.d("Adapter", "newsArticleList[position].media :${newsArticleList[position].media}")
            if( newsArticleList[position].media != null ){
                imageUri = Uri.parse(newsArticleList[position].media).toString()
            }

            Utils.loadAndSetImageWithGlide(
                holder.item.context,
                holder.item.findViewById<ImageView>(R.id.item_image),
                imageUri
            )

            holder.item.findViewById<TextView>(R.id.item_rating).text = newsArticleList[position].rank.toString()


            holder.item.setOnClickListener {
                val bundle = bundleOf( "article" to newsArticleList[position])
                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle )

            }

        }

    }
}