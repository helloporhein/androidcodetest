package com.example.acepluscodetest

import android.app.DownloadManager
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.acepluscodetest.databinding.ActivityWebBinding
import com.example.acepluscodetest.view.activities.PhotosActivity

class WebActivity : AppCompatActivity() {

    var web: WebView? = null
    var progressBar: ProgressBar? = null
    var mEmptyStateView: TextView? = null
    var toolbar: Toolbar? = null
    var url: String? = null
    //private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        progressBar = findViewById(R.id.loading_indicator)
        mEmptyStateView = findViewById(R.id.empty_view)
        progressBar!!.setVisibility(View.VISIBLE)
        web = findViewById(R.id.webView)
        web?.setWebViewClient(Browser_home())
        // For Video
        //web?.setWebChromeClient(MyChrome())
        val webSettings = web!!.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        url = "https://aceplussolutions.com/"
        //web!!.settings.cacheMode=WebSettings.LOAD_NO_CACHE
        web?.settings?.cacheMode=WebSettings.LOAD_CACHE_ELSE_NETWORK
        if (savedInstanceState == null) {
            web?.post(Runnable { loadWebsite() })
        }
        //For Download
        web?.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val myRequest = DownloadManager.Request(Uri.parse(url))
            myRequest.allowScanningByMediaScanner()
            myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val myManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            myManager.enqueue(myRequest)
            Toast.makeText(this@WebActivity, "Your download is started", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onBackPressed() {
        if (web?.canGoBack() == true) {
            web?.goBack()
        } else {
            //super.onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web?.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        web?.restoreState(savedInstanceState)
    }

    private fun loadWebsite() {
        val cm = application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected) {
            //val intent = intent
            //val website = intent.getStringExtra("links")

            val website = url
            if (website != null) {
                web?.loadUrl(website)
            }
        } else {
            progressBar!!.visibility = View.GONE
            mEmptyStateView!!.setText(R.string.no_internet)
        }
    }
    /*
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }*/

    internal inner class Browser_home : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            title = view.title
            progressBar!!.visibility = View.GONE
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (url == "https://aceplussolutions.com/events-gallery/") {
                val intent = Intent(applicationContext, PhotosActivity::class.java)
                startActivity(intent)
                true // Handle By application itself
            } else {
                view.loadUrl(url)
                true
            }
        }
    }

    /*
    private inner class MyChrome internal constructor() : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemVisibility
            requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemVisibility = window.decorView.systemUiVisibility
            mOriginalOrientation = requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (window.decorView as FrameLayout).addView(mCustomView, FrameLayout.LayoutParams(-1, -1))
            window.decorView.systemUiVisibility = 3846
        }
    }*/
}