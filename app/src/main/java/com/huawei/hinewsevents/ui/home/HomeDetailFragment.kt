package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.utils.extension.DialogUtils
import com.huawei.hinewsevents.utils.extension.PermissionUtils
import com.huawei.hinewsevents.utils.extension.PrefUtils
import com.huawei.hinewsevents.utils.extension.Utils
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView


class HomeDetailFragment : Fragment() {

    //region "object and view variables"
    val TAG: String = HomeDetailFragment::class.simpleName.toString()

    var adParam: AdParam? = null
    // for banner app
    private val REFRESH_TIME = 5
    private var adFrameLayout: FrameLayout? = null
    private var bannerView: BannerView? = null
    // for interstitial app
    private lateinit var fab: FloatingActionButton
    private var fabStatusTest: Boolean = false

    lateinit var app_bar_image : ImageView
    lateinit var tv_newsDetail_categories : TextView
    lateinit var tv_newsDetail_dateTime : TextView
    lateinit var tv_newsDetail_title : TextView
    lateinit var tv_newsDetail_content : TextView
    lateinit var tv_newsDetail_rating : TextView
    lateinit var rb_newsDetail_rating : RatingBar

    lateinit var cv_btn_fontSize: CardView
    lateinit var cv_btn_share: CardView
    lateinit var cv_btn_bookmark: CardView

    private lateinit var shareLink: String
    private lateinit var btn_showNewSource: Button

    lateinit var imageUri: String

    lateinit var recyclerviewListMedia: RecyclerView

    // endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home_detail, container, false)

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(context)

        initializeUI(view)

        if( Utils.haveNetworkConnection(view.context)){
            loadDefaultBannerAd()
        }

        loadViewsFromBundleValues(view)

        btn_showNewSource = view.findViewById(R.id.btn_newsDetail_showNewsSource)
        btn_showNewSource.setOnClickListener {
            // TODO set and edit bundle content
            val bundle = bundleOf( "link" to shareLink )
            Log.d(TAG,"btn_showNewSource item.findNavController().currentDestination ${view.findNavController().currentDestination} " +
                    " ${view.findNavController().currentDestination?.id} - homeDetailFragment ${R.id.homeDetailFragment}" )
            Navigation.findNavController(view).navigate(R.id.action_navigation_homeDetailFragment_to_webViewFragment, bundle )
        }

        app_bar_image.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if( event?.action == MotionEvent.ACTION_DOWN ) {
                    Log.d(TAG, "event?.action MotionEvent.ACTION_DOWN")
                    DialogUtils.showDialogImagePeekPopView(
                        app_bar_image.context,
                        imageUri,
                        tv_newsDetail_title.text.toString()
                    )
                    return true
                }else if( event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL ||
                    event?.action == MotionEvent.ACTION_HOVER_MOVE ||
                    event?.action == MotionEvent.ACTION_HOVER_EXIT ) {
                    Log.d(TAG, "ACTION_UP . ACTION_CANCEL .or. ACTION_HOVER_MOVE . ACTION_HOVER_EXIT .")
                    DialogUtils.hideQuickView()
                    return true
                }
                return false
            }
        })

        return view
    }

    private fun initializeUI(containerView: View) {

        adFrameLayout = containerView.findViewById<FrameLayout>(R.id.ad_frame)

        bannerView = containerView.findViewById<BannerView>(R.id.hw_banner_view)

        adParam = AdParam.Builder().build()


        fab = containerView.findViewById(R.id.fab)
        fab.setOnClickListener {
            Log.d(TAG, "fab.setOnClickListener")
            if( Utils.haveNetworkConnection(it.context)){
                reLoadBannerAd()
                if (fabStatusTest) {
                    fab.setImageResource(R.drawable.star_false)
                    fabStatusTest = false
                } else {
                    fab.setImageResource(R.drawable.star_true)
                    fabStatusTest = true
                }
            }
        }

        app_bar_image = containerView.findViewById(R.id.app_bar_image)
        tv_newsDetail_categories = containerView.findViewById(R.id.tv_newsDetail_categories)
        tv_newsDetail_dateTime = containerView.findViewById(R.id.tv_newsDetail_dateTime)
        tv_newsDetail_title = containerView.findViewById(R.id.tv_newsDetail_title)
        tv_newsDetail_content = containerView.findViewById(R.id.tv_newsDetail_content)
        tv_newsDetail_rating = containerView.findViewById(R.id.tv_newDetail_rating)
        rb_newsDetail_rating = containerView.findViewById(R.id.rb_newsDetails_rating)

        recyclerviewListMedia = containerView.findViewById(R.id.recyclerviewList_media)
        recyclerviewListMedia.visibility = View.GONE

        changeFontSizeWithPref()

        cv_btn_fontSize = containerView.findViewById(R.id.cv_detail_fontSize)
        cv_btn_fontSize.setOnClickListener {
            updateFontSize(it.context, PrefUtils.getPreferencesFontSize(it.context))
        }

        cv_btn_share = containerView.findViewById(R.id.cv_detail_share)
        cv_btn_share.setOnClickListener {
            //Utils.showToastMessage( it.context,"Show Share Type Select Dialog and Share News Content\n${shareLink}" )
            // Permission Tests
            //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            //    Log.i(TAG, "sdk < 28 Q call requestPermissions Location")
            //    requestPermissions(
            //        PermissionUtils.permissionRequestsLocation,
            //        PermissionUtils.requestCodeLocation
            //    )
            //} else {
            //    Log.i(TAG, "sdk > 28 Q call requestPermissions BackgroundLocation")
            //    requestPermissions(
            //        PermissionUtils.permissionRequestsBackgroundLocation,
            //        PermissionUtils.requestCodeBackgroundLocation
            //    )
            //}
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "from Hi-News: ${tv_newsDetail_title.text} $shareLink"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi News")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        cv_btn_bookmark = containerView.findViewById(R.id.cv_detail_bookmark)
        cv_btn_bookmark.visibility = View.GONE
        cv_btn_bookmark.setOnClickListener {
            Utils.showToastMessage( it.context, "News Content Save to Bookmark" )
            // Permission Tests
            //Log.i(TAG, "call requestPermissions CameraAndGallery")
            //requestPermissions(
            //    PermissionUtils.permissionRequestsCameraAndGallery,
            //    PermissionUtils.requestCodeCameraAndGallery
            //)
        }
    }


    private fun loadViewsFromBundleValues(view: View) {

        Log.d(TAG, "loadViewsFromBundleValues")

        if( arguments == null || requireArguments().isEmpty ){
            Log.d(TAG, "bundle arguments is NULL")
        }else{
            val newsArticle : Article = arguments?.getSerializable("article") as Article

            var bundleValueLink = "noLink"
            if( newsArticle.link != null ) bundleValueLink = newsArticle.link

            var bundleValueCategory = "Category : Huawei"
            if (newsArticle.topic != null) bundleValueCategory =
                "Category : Huawei - ${newsArticle.topic}"

            var bundleValueDatetime = "noDateTime"
            if (newsArticle.published_date != null) bundleValueDatetime = newsArticle.published_date

            var bundleValueTitle = "noTitle"
            if (newsArticle.title != null) bundleValueTitle = newsArticle.title

            var bundleValueContents = "noContent"
            if (newsArticle.summary != null) bundleValueContents = newsArticle.summary

            var bundleValueImageUri = Utils.getDefaultImageUri(view.context, R.drawable.notfound.toString())
            if (newsArticle.media != null) bundleValueImageUri = newsArticle.media

            var bundleValueRating = 1
            if (newsArticle.rank != null) bundleValueRating = newsArticle.rank

            shareLink = bundleValueLink

            tv_newsDetail_categories.text = bundleValueCategory
            tv_newsDetail_dateTime.text = bundleValueDatetime
            tv_newsDetail_title.text = bundleValueTitle
            tv_newsDetail_content.text = bundleValueContents
            Utils.loadAndSetImageWithGlide(context, app_bar_image, bundleValueImageUri)

            tv_newsDetail_rating.text =  bundleValueRating.toString()
            rb_newsDetail_rating.rating = bundleValueRating.toFloat()

            tv_newsDetail_rating.setTextColor( Utils.getColorRatingLevel( bundleValueRating.toInt() ) )
            rb_newsDetail_rating.progressTintList = (ColorStateList.valueOf( view.context.resources.getColor( Utils.getColorRatingLevel( bundleValueRating.toInt() ) ) ) )

            if( newsArticle.media_content == null ){
                Log.d(TAG, "News has not any media Content Url")
            }else{
                var mediaUrlsAll: MutableList<String> = newsArticle.media_content.split(",").map { it.trim() } as MutableList<String>
                var mediaUrlsWithoutSvg = mutableListOf<String>()
                for (i in mediaUrlsAll.indices){
                    Log.d(TAG, "mediaUrlsAll [$i] : ${mediaUrlsAll[i]}\n")
                    if(mediaUrlsAll[i].endsWith(".svg")){
                        Log.d(TAG, "(mediaUrlsAll[$i].endsWith .svg and remove it from list, cause thats not supported with Glide\n")
                    }else{
                        mediaUrlsWithoutSvg.add(mediaUrlsAll[i])
                    }
                }
                recyclerviewListMedia.adapter = NewsMediaImagesAdapter(mediaUrlsWithoutSvg)
                recyclerviewListMedia.visibility = View.VISIBLE
            }

            imageUri = bundleValueImageUri
        }


    }



    private fun loadDefaultBannerAd() {
        Log.d(TAG, "loadDefaultBannerAd()....")
        // Load the default banner ad.
        bannerView = BannerView(context)
        bannerView!!.adId = "testw6vs28auh3"
        bannerView!!.setBackgroundColor(Utils.getBannerViewBackground( 0 )) // Utils.getRandomNumber(6, 1)
        bannerView!!.bannerAdSize = Utils.getBannerAdSize( 8 ) // 2,5 //  Utils.getRandomNumber(8, 1)
        bannerView!!.setBannerRefresh(REFRESH_TIME.toLong())
        bannerView!!.adListener = adListener
        bannerView!!.loadAd(adParam)
        adFrameLayout!!.addView(bannerView)
    }

    private fun reLoadBannerAd() {
        Log.d(TAG, "reLoadBannerAd()....")
        if (bannerView != null) {
            adFrameLayout!!.removeView(bannerView)
            bannerView!!.destroy()
        }
        Log.d(TAG, "reLoadBannerAd : create new bannerView; ")
        bannerView = BannerView(context)
        bannerView!!.adId = "testw6vs28auh3"
        // bannerView.setAdId( String.valueOf( R.string.ad_id_banner) );    // getting onAdFailed, errorCode:700 with error code 3.
        bannerView!!.setBackgroundColor(Utils.getBannerViewBackground( Utils.getRandomNumber(6, 1) ))
        bannerView!!.bannerAdSize = Utils.getBannerAdSize( Utils.getRandomNumber(8, 1) )
        bannerView!!.setBannerRefresh(REFRESH_TIME.toLong())
        bannerView!!.adListener = adListener
        bannerView!!.loadAd(adParam)
        adFrameLayout!!.addView(bannerView)
    }


    private fun changeFontSizeWithPref(){
        tv_newsDetail_title.textSize = PrefUtils.getPreferencesFontSize(context).toFloat()
        tv_newsDetail_content.textSize = PrefUtils.getPreferencesFontSize(context).toFloat()
    }

    fun updateFontSize(context: Context, fontSize: Int) {

        var updateFontSizeDialog: AlertDialog
        val updateFontSizeDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        val mView = layoutInflater.inflate(R.layout.dialog_change_font_size, null)

        var seekBarValue: Int = 24 // default
        lateinit var seekbar: SeekBar

        val fontSizePreview: TextView

        val btnSave: Button
        val btnCancel: Button

        fontSizePreview = mView.findViewById(R.id.tv_font_size_preview)
        fontSizePreview.textSize = fontSize.toFloat()

        btnSave = mView.findViewById(R.id.btnOk)
        btnCancel = mView.findViewById(R.id.btnCancel)

        updateFontSizeDialogBuilder.setView(mView)
        updateFontSizeDialog = updateFontSizeDialogBuilder.create()
        updateFontSizeDialog.window!!.setWindowAnimations(R.style.DialogAnimation_UpBottom)
        updateFontSizeDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        updateFontSizeDialog.setCanceledOnTouchOutside(false)
        updateFontSizeDialog.show()

        updateFontSizeDialog.setOnKeyListener(
            DialogInterface.OnKeyListener { dialog, keyCode, event ->

                Log.d(TAG, "event : $event - keyCode : $keyCode")

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                }
                return@OnKeyListener true
            }
        )

        seekbar = mView.findViewById(R.id.seekbar_font_size)
        when (fontSize) {
            18 -> seekbar.progress = 0
            20 -> seekbar.progress = 1
            22 -> seekbar.progress = 2
            24 -> seekbar.progress = 3
            26 -> seekbar.progress = 4
            28 -> seekbar.progress = 5
            30 -> seekbar.progress = 6
            32 -> seekbar.progress = 7
            34 -> seekbar.progress = 8
        }

        seekbar.setOnSeekBarChangeListener(object :

            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                when (progress) {
                    0 -> seekBarValue = 18
                    1 -> seekBarValue = 20
                    2 -> seekBarValue = 22
                    3 -> seekBarValue = 24
                    4 -> seekBarValue = 26
                    5 -> seekBarValue = 28
                    6 -> seekBarValue = 30
                    7 -> seekBarValue = 32
                    8 -> seekBarValue = 34
                }
                fontSizePreview.textSize = seekBarValue.toFloat()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            updateFontSizeDialog.dismiss()
        })

        btnSave.setOnClickListener(View.OnClickListener {
            PrefUtils.setPreferencesFontSize(it.context, seekBarValue)
            changeFontSizeWithPref()
            updateFontSizeDialog.dismiss()
        })

    }


    /**
     * Ad listener.
     */
    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Called when an ad is loaded successfully.
            Log.d(TAG, "adListener.onAdLoaded() ")
            //Utils.showToastMessage( context,"Ad loaded.");
        }

        override fun onAdFailed(errorCode: Int) {
            Log.e( TAG,"adListener.onAdFailed() with error code $errorCode")
            // Called when an ad fails to be failed.
            Utils.showToastMessage(context,"Ad failed to load with error code $errorCode")
        }

        override fun onAdOpened() {
            Log.d(TAG, "adListener.onAdOpened() ")
            // Called when an ad is opened.
            Utils.showToastMessage(context,"Ad opened ")
        }

        override fun onAdClicked() {
            // Called when a user taps an ad.
            Log.d(TAG, "adListener.onAdClicked() ")
            Utils.showToastMessage(context,"Ad clicked")
        }

        override fun onAdLeave() {
            // Called when a user has left the app.
            Log.d(TAG, "adListener.onAdLeave() ")
            Utils.showToastMessage(context,"Ad Leave ")
        }

        override fun onAdClosed() {
            Log.d(TAG, "adListener.onAdClosed() ")
            // Called when an ad is closed.
            Utils.showToastMessage(context,"Ad closed")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult : requestCode : $requestCode")

        when(requestCode){

            PermissionUtils.requestCodeCameraAndGallery ->
                if (grantResults.size > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i(TAG, "onRequestPermissionsResult: CameraAndGallery permission is successful")
                    Utils.showToastMessage(context,"CameraAndGallery permission is successful.\nYou Can Do Something with this.")
                    // Do Something with camera and gallery intent and edit onActivityResult accordingly

                } else {
                    Log.i(TAG, "onRequestPermissionsResult: CameraAndGallery permission is not granted")

                    Utils.showDialogOK(context,"NEED CAMERA/GALLERY PERMISSION",
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                DialogInterface.BUTTON_NEGATIVE ->
                                    Utils.showToastMessage(context,"CameraAndGallery permission is Need!")
                            }
                        })
                }

            PermissionUtils.requestCodeLocation ->
                if (grantResults.size > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i(TAG, "onRequestPermissionsResult: Location permission is successful")
                    Utils.showToastMessage(context,"Location permission is successful.\nYou Can Do Something with this.")
                    // Do Something with location intent and edit onActivityResult accordingly

                } else {
                    Log.i(TAG, "onRequestPermissionsResult: Location permission is not granted")

                    Utils.showDialogOK(context,"NEED LOCATION PERMISSION",
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                DialogInterface.BUTTON_NEGATIVE ->
                                    Utils.showToastMessage(context,"Location permission is Need!")
                            }
                        })
                }

            PermissionUtils.requestCodeBackgroundLocation ->
                if (grantResults.size > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i(TAG, "onRequestPermissionsResult: Background Location permission is successful")
                    Utils.showToastMessage(context,"BackgroundLocation permission is successful.\nYou Can Do Something with this.")
                    // Do Something with backgroundLocation and edit onActivityResult accordingly

                } else {
                    Log.i(TAG, "onRequestPermissionsResult: BackgroundLocation permission is not granted")

                    Utils.showDialogOK(context,"NEED BACKGROUND LOCATION PERMISSION",
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                DialogInterface.BUTTON_NEGATIVE ->
                                    Utils.showToastMessage(context,"Background Location permission is Need!")
                            }
                        })
                }

            else -> Log.i(TAG, "onRequestPermissionsResult: undefined requestCode : $requestCode")

        }// end of when

    }// end of onRequestPermissionsResult

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