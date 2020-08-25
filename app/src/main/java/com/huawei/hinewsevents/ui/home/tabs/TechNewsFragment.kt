package com.huawei.hinewsevents.ui.home.tabs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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

    var currentPage :Int = 1
    var totalPage :Int = 3 // default is greater the currentPage //

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
                showErrorLayout(view)
            }
        }

        setInitRecyclerView(view)
        //setRecyclerViewScrollListener(view)

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

    // TODO check and arrange this pagining for infinity scroll and edit other tabs like this
    private fun setRecyclerViewScrollListener(view: View) {
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //super.onScrolled(recyclerView, dx, dy)

                val totalItemCount: Int = layoutManager.itemCount
                val visibleItemCount: Int = layoutManager.childCount
                val firstVisibleItem: Int = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()

                if(techNewsViewModel!!.techNewsListLoading.value!!){
                    Log.d(TAG, "recyclerView.addOnScrollListener techNewsViewModel!!.techNewsListLoading")
                    showProgress(view)
                }else{

                    // TODO check this only first 4/5 item showing on screen
                    if( currentPage == 1 ){
                        if ( totalItemCount - lastVisibleItem < 2) {
                            Log.d(TAG, "recyclerView.addOnScrollListener page1 totalItemCount $totalItemCount - lastVisibleItem $lastVisibleItem < 2")
                            currentPage++
                            //getViewModelAndSetAdapter(view)
                        }
                    }else{
                        if ( totalItemCount - lastVisibleItem <= 3) {
                            Log.d(TAG, "recyclerView.addOnScrollListener totalItemCount $totalItemCount - lastVisibleItem $lastVisibleItem <= 3")
                            currentPage++
                            //getViewModelAndSetAdapter(view)
                        }
                    }

                }


            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun getViewModelAndSetAdapter(view: View) {

        showProgress(view)

        if(currentPage <= totalPage){

            techNewsViewModel = ViewModelProviders.of(this).get(TechNewsViewModel::class.java)
            techNewsViewModel!!.refreshData(currentPage)
            //observe viewModel live data
            techNewsViewModel!!.techNewsListData.observe(viewLifecycleOwner, Observer {
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

                        recyclerView.adapter = TechNewsAdapter( it.articles )

                        if( ivError.isVisible ) hideErrorLayout(view)

                        hideProgress(view)
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            })

        } else {
            hideProgress(view)
            Utils.showToastMessage(view.context,"No more pages! ")
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


    class TechNewsAdapter(private val newsArticleList: List<Article>) :
        RecyclerView.Adapter<TechNewsAdapter.ViewHolder>() {

        val TAG: String = TechNewsAdapter::class.simpleName.toString()

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

                Log.d(TAG, "link     :${newsArticleList[position].link}")
                Log.d(TAG, "id       :${newsArticleList[position].id}")
                Log.d(TAG, "rating   :${newsArticleList[position].rank}")
                Log.d(TAG, "category :${newsArticleList[position].topic}")
                Log.d(TAG, "dateTime :${newsArticleList[position].published_date}")
                Log.d(TAG, "title    :${newsArticleList[position].title}")
                Log.d(TAG, "contents :${newsArticleList[position].summary}")
                Log.d(TAG, "imageUri :${newsArticleList[position].media}")

                Log.d(TAG,"onBindViewHolder item onCLick and item.findNavController().currentDestination ${holder.item.findNavController().currentDestination} " +
                        " ${holder.item.findNavController().currentDestination?.id} - navigation_home ${R.id.navigation_home}" )
                // TODO set and edit bundle content
                val bundle = bundleOf(
                    "link" to newsArticleList[position].link,
                    "rating" to newsArticleList[position].rank,
                    "category" to newsArticleList[position].topic,
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