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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.viewmodel.MainNewsViewModel
import com.huawei.hinewsevents.utils.extension.Utils

class MainNewsFragment : Fragment() {

    val TAG: String = MainNewsFragment::class.simpleName.toString()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivError: ImageView
    private lateinit var progress: ProgressBar

    private var mainNewsViewModel: MainNewsViewModel? = null

    private var currentPage: Int = 1
    private var totalPage: Int = 2 // default is greater the currentPage //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main_news, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        ivError = view.findViewById(R.id.iv_error)
        progress = view.findViewById(R.id.progressBar)

        swipeRefreshLayout.setOnRefreshListener {
            if (Utils.haveNetworkConnection(view.context)) {
                swipeRefreshLayout.isRefreshing = true
                getViewModelAndSetAdapter(view)
            } else {
                Utils.showToastMessage(view.context, "Need Network Connection!")
                showErrorLayout(view)
            }
        }

        setInitRecyclerView(view)

        // first fetch
        if (Utils.haveNetworkConnection(view.context)) {
            getViewModelAndSetAdapter(view)
        } else {
            Utils.showToastMessage(view.context, "Need Network Connection!")
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

        showProgress(view)

        if(currentPage <= totalPage){

            mainNewsViewModel = ViewModelProviders.of(this).get(MainNewsViewModel::class.java)
            mainNewsViewModel!!.refreshData(currentPage)
            //observe viewModel live data
            mainNewsViewModel!!.mainNewsListData.observe(viewLifecycleOwner, Observer {
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

                        Log.d(TAG, "getViewModelAndSetAdapter : it.Page  :${currentPage} /${totalPage}")

                        recyclerView.adapter = MainNewsAdapter(it.articles)

                        if( ivError.isVisible ) hideErrorLayout(view)

                        hideProgress(view)
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            })

        } else {
            hideProgress(view)
            Utils.showToastMessage(view.context, "No more pages! ")
        }

    }

    private fun showProgress(view: View){
        progress.visibility = View.VISIBLE
    }
    private fun hideProgress(view: View){
        progress.visibility = View.GONE
        // if swiped
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorLayout(view: View){
        ivError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hideProgress(view)
    }

    private fun hideErrorLayout(view: View){
        ivError.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


    class MainNewsAdapter(private val newsArticleList: List<Article>) :
        RecyclerView.Adapter<MainNewsAdapter.ViewHolder>() {

        val TAG: String = MainNewsAdapter::class.simpleName.toString()

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

            holder.item.findViewById<TextView>(R.id.item_date_time).text =
                newsArticleList[position].published_date

            holder.item.findViewById<TextView>(R.id.item_title).text =
                newsArticleList[position].title

            holder.item.findViewById<TextView>(R.id.item_detail).text =
                newsArticleList[position].summary

            var imageUri: String = Uri.parse(
                "android.resource://" +
                        holder.item.context.packageName + "/" +
                        R.drawable.notfound.toString()
            ).toString()

            //Log.d(TAG, "newsArticleList[position].media :${newsArticleList[position].media}")
            if (newsArticleList[position].media != null) {
                imageUri = Uri.parse(newsArticleList[position].media).toString()
            }

            Utils.loadAndSetImageWithGlide(
                holder.item.context,
                holder.item.findViewById<ImageView>(R.id.item_image),
                imageUri
            )

            holder.item.findViewById<TextView>(R.id.item_rating).text =
                newsArticleList[position].rank.toString()


            holder.item.setOnClickListener {

                // TODO check and remove
                Log.d(TAG, "link     :${newsArticleList[position].link}")
                Log.d(TAG, "id       :${newsArticleList[position].id}")
                Log.d(TAG, "rating   :${newsArticleList[position].rank}")
                Log.d(TAG, "dateTime :${newsArticleList[position].published_date}")
                Log.d(TAG, "title    :${newsArticleList[position].title}")
                Log.d(TAG, "contents :${newsArticleList[position].summary}")
                Log.d(TAG, "imageUri :${newsArticleList[position].media}")

                Log.d(
                    TAG,
                    "onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                            " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}"
                )
                // TODO set and edit bundle content
                val bundle = bundleOf(
                    "link" to newsArticleList[position].link,
                    "rating" to newsArticleList[position].rank,
                    "dateTime" to newsArticleList[position].published_date,
                    "title" to newsArticleList[position].title,
                    "contents" to newsArticleList[position].summary,
                    "imageUri" to newsArticleList[position].media
                )

                Navigation.findNavController(holder.itemView)
                    .navigate(R.id.action_navigation_home_to_homeDetailFragment, bundle)
                //holder.item.findNavController().navigate( R.id.action_navigation_home_to_homeDetailFragment )

            }

        }

    }

}