package app.spidy.orb

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import app.spidy.orb.fragments.OrbBrowserFragment
import app.spidy.orb.interfaces.BrowserListener

open class OrbActivity: AppCompatActivity(), BrowserListener {
    private var browserFragment: OrbBrowserFragment? = null

    fun getOrbFragment(): OrbBrowserFragment {
        browserFragment = OrbBrowserFragment()
        return browserFragment!!
    }

    fun restoreOrbFragment(): OrbBrowserFragment? {
        browserFragment = supportFragmentManager.findFragmentByTag("OrbBrowserFragment") as? OrbBrowserFragment
        return browserFragment
    }

    override fun onPageTap() {
        browserFragment?.resetTabMode()
    }

    override fun onNewTab(url: String) {
        browserFragment?.onNewTab(url)
    }

    override fun onProgressChanged(webView: WebView?, progress: Int) {
        browserFragment?.onProgressChanged(webView, progress)
    }

    override fun onPageStarted(view: WebView?, url: String?) {
        browserFragment?.onPageStarted(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        browserFragment?.onPageFinished(view, url)
    }
}