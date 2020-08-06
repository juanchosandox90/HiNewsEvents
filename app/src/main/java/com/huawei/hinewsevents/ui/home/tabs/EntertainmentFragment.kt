package com.huawei.hinewsevents.ui.home.tabs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.viewmodel.EntertainmentViewModel
import com.huawei.hinewsevents.utils.extension.Utils

class EntertainmentFragment : Fragment() {

    val TAG: String = EntertainmentFragment::class.simpleName.toString()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView

    private var entertainmentNewsViewModel: EntertainmentViewModel? = null

    var currentPage :Int = 1
    var totalPage :Int = 2 // default is greater the currentPage //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_entertainment, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        swipeRefreshLayout.setOnRefreshListener {
            if( Utils.haveNetworkConnection(view.context) ) {
                swipeRefreshLayout.isRefreshing = true
                getViewModelAndSetAdapter(view)
            }else{
                Utils.showToastMessage(view.context,"Need Network Connection!")
                showErrorLayout(view)
            }
        }

        setInitRecyclerView(view)

        // first fetch
        if( Utils.haveNetworkConnection(view.context) ) {
            getViewModelAndSetAdapter(view)
        }else{
            Utils.showToastMessage(view.context,"Need Network Connection!")
            showErrorLayout(view)
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

        if(currentPage <= totalPage ){

            showProgress(view)

            entertainmentNewsViewModel = ViewModelProviders.of(this).get(EntertainmentViewModel::class.java)
            entertainmentNewsViewModel!!.refreshData(currentPage)
            //observe viewModel live data
            entertainmentNewsViewModel!!.entertainmentNewsListData.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    showErrorLayout(view)
                } else {
                    //Log.d(TAG, "it  :${it.toString()}")
                    var status: String = it.status
                    if( status != "ok"){
                        Utils.showToastMessage(context, status)
                    }else{

                        totalPage = it.total_pages
                        currentPage = it.page

                        Log.d(TAG, "it Page  :${currentPage} / :${totalPage}")

                        hideErrorLayout(view)

                        recyclerView.adapter = EntertainmentNewsAdapter( it.articles )
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            })
            hideProgress(view)
        }else{
            Utils.showToastMessage(view.context,"No more pages! ")
        }

    }

    private fun showProgress(view: View){
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    }
    private fun hideProgress(view: View){
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
    }

    private fun showErrorLayout(view: View){
        view.findViewById<RecyclerView>(R.id.recyclerview_list).visibility = View.GONE
        view.findViewById<ImageView>(R.id.iv_error).visibility = View.VISIBLE
    }

    private fun hideErrorLayout(view: View){
        view.findViewById<RecyclerView>(R.id.recyclerview_list).visibility = View.VISIBLE
        view.findViewById<ImageView>(R.id.iv_error).visibility = View.GONE
    }


    class EntertainmentNewsAdapter(private val newsArticleList: List<Article>) :
        RecyclerView.Adapter<EntertainmentNewsAdapter.ViewHolder>() {

        val TAG: String = EntertainmentNewsAdapter::class.simpleName.toString()

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

            var imageUri: String = Uri.parse(
                "android.resource://" +
                        holder.item.context.packageName + "/" +
                        R.drawable.notfound.toString()
            ).toString()

            //Log.d(TAG, "newsArticleList[position].media :${newsArticleList[position].media}")
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

                // TODO check and remove
                Log.d(TAG, "id       :${newsArticleList[position].id}")
                Log.d(TAG, "rating   :${newsArticleList[position].rank}")
                Log.d(TAG, "dateTime :${newsArticleList[position].published_date}")
                Log.d(TAG, "title    :${newsArticleList[position].title}")
                Log.d(TAG, "contents :${newsArticleList[position].summary}")
                Log.d(TAG, "imageUri :${newsArticleList[position].media}")
                Log.d(TAG,"onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                        " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}" )
                // TODO set and edit bundle content
                val bundle = bundleOf(
                    "rating" to newsArticleList[position].rank,
                    "dateTime" to newsArticleList[position].published_date,
                    "title" to newsArticleList[position].title,
                    "contents" to newsArticleList[position].summary,
                    "imageUri" to newsArticleList[position].media
                )

                Navigation.findNavController(holder.itemView).navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle )
                //holder.item.findNavController().navigate( R.id.action_navigation_home_to_homeDetailFragment )

            }

        }

    }

}