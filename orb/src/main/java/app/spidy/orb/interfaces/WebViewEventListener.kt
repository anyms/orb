package app.spidy.orb.interfaces

import android.graphics.Bitmap
import android.webkit.WebView

interface WebViewEventListener {
    fun onNewTab(url: String) {}
    fun onReceivedTitle(view: WebView?, title: String) {}
    fun onReceivedIcon(view: WebView?, icon: Bitmap?) {}
    fun onProgressChanged(view: WebView?, progress: Int) {}
    fun onCloseWindow(view: WebView?) {}
    fun onPageStarted(view: WebView?, url: String?) {}
    fun onPageFinished(view: WebView?, url: String?) {}
}