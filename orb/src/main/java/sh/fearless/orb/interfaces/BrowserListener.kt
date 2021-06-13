package sh.fearless.orb.interfaces

import android.webkit.WebView

interface BrowserListener {
    fun onPageTap() {}
    fun onNewTab(url: String) {}
    fun onProgressChanged(webView: WebView?, progress: Int) {}
    fun onPageStarted(view: WebView?, url: String?) {}
    fun onPageFinished(view: WebView?, url: String?) {}
}