package com.huawei.hinewsevents.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.huawei.hinewsevents.R
import com.huawei.hinewsevents.utils.extension.Utils


class WebViewFragment : Fragment() {

    //region "object and view variables"
    val TAG: String = WebViewFragment::class.simpleName.toString()

    private lateinit var ivError: ImageView
    private lateinit var progress: ProgressBar

    private lateinit var shareLink: String
    private lateinit var webView: WebView

    // endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "3 onCreateView")

        val view = inflater.inflate(R.layout.fragment_home_detail_webview, container, false)

        webView = view.findViewById(R.id.webview)
        ivError = view.findViewById(R.id.iv_error)
        progress = view.findViewById(R.id.progressBar)

        getLinkFromBundle(view)

        if( Utils.haveNetworkConnection(view.context)){
            setupWebView(view)
        }else{
            Utils.showToastMessage(view.context,"Need Network Connection!")
            showErrorLayout()
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d(TAG, "3 onCreateView : webView onPageStarted ")
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.d(TAG, "3 onCreateView : webView onPageFinished ")
                progress.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Log.d(TAG, "3 onCreateView : webView onReceivedError errorCode : $errorCode description : $description")
                showErrorLayout()
                Utils.showToastMessage(webView.context,"onReceivedError! $description")
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        return view
    }

    private fun getLinkFromBundle(view: View) {
        Log.d(TAG, "loadViewsFromBundleTestValues")
        if( arguments == null || requireArguments().isEmpty ){
            Log.d(TAG, "bundle arguments is NULL")
        }else{
            var bundleValueLink :String = arguments?.getString("link", "noValueLink").toString()

            Log.d(TAG, "bundleValueLink     :$bundleValueLink}")

            shareLink = bundleValueLink
        }
    }

    private fun setupWebView(view: View){

        webView.loadUrl(shareLink)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        @SuppressLint("SetJavaScriptEnabled")
        webView.settings.javaScriptEnabled = true

        webView.canGoBackOrForward(3)

        Log.d(TAG, "appCachePath     :${view.context.cacheDir.absolutePath}")
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.setAppCachePath(view.context.cacheDir.absolutePath)
        webView.settings.domStorageEnabled = true

    }

    private fun showErrorLayout(){
        Log.d(TAG, "showErrorLayout")
        webView.visibility = View.GONE
        ivError.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    private fun hideErrorLayout(){
        webView.visibility = View.VISIBLE
        ivError.visibility = View.GONE
    }


    override fun onAttach(context: Context) {
        Log.d(TAG, "1 onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "2 onCreate")
        if( arguments == null || requireArguments().isEmpty ){
            Log.d(TAG, "onCreate bundle arguments is NULL")
        }else{
            var bundleValueLink :String = arguments?.getString("link", "noValueLink").toString()

            Log.d(TAG, "onCreate bundleValueLink     :$bundleValueLink}")
            shareLink = bundleValueLink
        }
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