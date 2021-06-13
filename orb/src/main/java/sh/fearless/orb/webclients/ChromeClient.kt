package sh.fearless.orb.webclients

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Message
import android.view.*
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import sh.fearless.orb.interfaces.WebViewEventListener


class ChromeClient(private val context: Context, private val listener: WebViewEventListener): WebChromeClient() {
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var originalOrientation: Int? = null
    private var originalSystemUiVisibility: Int? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        if (title != null) listener.onReceivedTitle(view, title)
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
//        browser?.also {
//            for (i in browser.tabs.indices) {
//                if (browser.tabs[i].fragment.webView == view) {
//                    browser.tabs[i].favIcon = icon
//                    break
//                }
//            }
//        }
        super.onReceivedIcon(view, icon)
        listener.onReceivedIcon(view, icon)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        listener.onProgressChanged(view, newProgress)
    }

    override fun onCloseWindow(window: WebView?) {
//        browser?.let {
//            for (i in it.tabs.indices) {
//                if (it.tabs[i].fragment.webView == window) {
//                    it.closeTab(i)
//                    break
//                }
//            }
//        }
        super.onCloseWindow(window)
        listener.onCloseWindow(window)
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val href = view?.handler?.obtainMessage()
        view?.requestFocusNodeHref(href)
        val url = href?.data?.getString("url")
        if (url != null) {
            listener.onNewTab(url)
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            onHideCustomView()
            return
        }

        customView = view
        customView?.setBackgroundColor(Color.BLACK)
        customViewCallback = callback

//        fullScreen()
        val content = (context as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content).rootView
        (content as ViewGroup).addView(customView)
        (context as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onHideCustomView() {
        val content = (context as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content).rootView
        (content as ViewGroup).removeView(customView)
        customView = null
        customViewCallback?.onCustomViewHidden()
        customViewCallback = null
        (context as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (context as AppCompatActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller: WindowInsetsController? = (context as AppCompatActivity).getWindow().getInsetsController()
            //BEFORE TOGGLE
//                    System.out.println(controller.getSystemBarsAppearance());
//                    System.out.println(controller.getSystemBarsBehavior());
            if (controller != null) {
                if (controller.systemBarsBehavior == 0) {
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    controller.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
                }
            }
        } else {
            val attrs: WindowManager.LayoutParams = (context as AppCompatActivity).getWindow().getAttributes()
            attrs.flags = attrs.flags xor WindowManager.LayoutParams.FLAG_FULLSCREEN
            (context as AppCompatActivity).getWindow().setAttributes(attrs)
        }
    }
}