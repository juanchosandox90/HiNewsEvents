package com.huawei.hinewsevents.ui.home

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.PrefUtils
import com.huawei.hinewsevents.utils.extension.Utils
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView


class HomeDetailFragment : Fragment() {

    //region "object and view variables"
    val TAG: String = "HomeDtlFragment"

    var adParam: AdParam? = null
    // for banner app
    private val REFRESH_TIME = 5
    private var adFrameLayout: FrameLayout? = null
    private var bannerView: BannerView? = null
    // for interstitial app
    private lateinit var fab: FloatingActionButton;
    private var fabStatusTest: Boolean = false

    lateinit var app_bar_image : ImageView
    lateinit var tv_newsDetail_dateTime : TextView
    lateinit var tv_newsDetail_title : TextView
    lateinit var tv_newsDetail_content : TextView
    lateinit var tv_newsDetail_rating : TextView
    lateinit var rb_newsDetail_rating : RatingBar

    lateinit var cv_btn_fontSize: CardView
    lateinit var cv_btn_share: CardView
    lateinit var cv_btn_bookmark: CardView

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

        loadViewsFromBundleTestValues(view)

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
        tv_newsDetail_dateTime = containerView.findViewById(R.id.tv_newsDetail_dateTime)
        tv_newsDetail_title = containerView.findViewById(R.id.tv_newsDetail_title)
        tv_newsDetail_content = containerView.findViewById(R.id.tv_newsDetail_content)
        tv_newsDetail_rating = containerView.findViewById(R.id.tv_newDetail_rating)
        rb_newsDetail_rating = containerView.findViewById(R.id.rb_newsDetails_rating)

        changeFontSizeWithPref()

        cv_btn_fontSize = containerView.findViewById(R.id.cv_detail_fontSize)
        cv_btn_fontSize.setOnClickListener {
            updateFontSize(it.context, PrefUtils.getPreferencesFontSize(it.context))
        }

        cv_btn_share = containerView.findViewById(R.id.cv_detail_share)
        cv_btn_share.setOnClickListener {
            Utils.showToastMessage( it.context,"Show Share Type Select Dialog and Share News Content" )
        }

        cv_btn_bookmark = containerView.findViewById(R.id.cv_detail_bookmark)
        cv_btn_bookmark.setOnClickListener {
            Utils.showToastMessage( it.context, "News Content Save to Bookmark" )
        }
    }


    private fun loadViewsFromBundleTestValues(view: View) {

        Log.d(TAG, "loadViewsFromBundleTestValues")

        if( arguments == null || requireArguments().isEmpty ){
            Log.d(TAG, "bundle arguments is NULL")
        }else{
            var bundleValueDatetime :String = arguments?.getString("dateTime", "noValueDateTime").toString()
            var bundleValueTitle :String = arguments?.getString("title", "noValueTitle").toString()
            var bundleValueContents :String = arguments?.getString("contents", "noValueContents").toString()
            var bundleValueImageUri :String = arguments?.getString("imageUri", "noValueImageUri").toString()
            var bundleValueRating :String = arguments?.getString("rating", "3.5").toString()

            Log.d(TAG, "bundleValueDatetime :$bundleValueDatetime}")
            Log.d(TAG, "bundleValueTitle    :$bundleValueTitle}")
            Log.d(TAG, "bundleValueContents :$bundleValueContents}")
            Log.d(TAG, "bundleValueImageUri :$bundleValueImageUri}")
            Log.d(TAG, "bundleValueRating   :$bundleValueRating}")

            tv_newsDetail_dateTime.text = bundleValueDatetime
            tv_newsDetail_title.text = bundleValueTitle
            tv_newsDetail_content.text = bundleValueContents
            Utils.loadAndSetImageWithGlide(context, app_bar_image, bundleValueImageUri)

            tv_newsDetail_rating.text = (bundleValueRating.toFloat() * 100).toString()
            rb_newsDetail_rating.rating = bundleValueRating.toFloat() / 2

            tv_newsDetail_rating.setTextColor( Utils.getColorRatingLevel( bundleValueRating.toFloat() ) )
            rb_newsDetail_rating.progressTintList = (ColorStateList.valueOf( view.context.resources.getColor( Utils.getColorRatingLevel( bundleValueRating.toFloat() ) ) ) )

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

        updateFontSizeDialogBuilder.setView(mView);
        updateFontSizeDialog = updateFontSizeDialogBuilder.create();
        updateFontSizeDialog.window!!.setWindowAnimations(R.style.DialogAnimation_UpBottom)
        updateFontSizeDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        updateFontSizeDialog.setCanceledOnTouchOutside(false)
        updateFontSizeDialog.show();

        updateFontSizeDialog.setOnKeyListener(
            DialogInterface.OnKeyListener { dialog, keyCode, event ->

                Log.d(TAG, "event : $event - keyCode : $keyCode")

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
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
            updateFontSizeDialog.dismiss();
        })

        btnSave.setOnClickListener(View.OnClickListener {
            PrefUtils.setPreferencesFontSize(it.context, seekBarValue)
            changeFontSizeWithPref()
            updateFontSizeDialog.dismiss();
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