package app.spidy.orb.interfaces

import android.webkit.WebView
import app.spidy.orb.Browser

interface BrowserListener {
    fun onPageTap() {}
    fun onNewTab(url: String) {}
    fun onProgressChanged(webView: WebView?, progress: Int) {}
    fun onPageStarted(view: WebView?, url: String?) {}
    fun onPageFinished(view: WebView?, url: String?) {}
}