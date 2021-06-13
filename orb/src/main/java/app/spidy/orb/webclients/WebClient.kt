package app.spidy.orb.webclients

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import app.spidy.orb.interfaces.WebViewEventListener

class WebClient(private val listener: WebViewEventListener): WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        listener.onPageStarted(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        listener.onPageFinished(view, url)
    }
}