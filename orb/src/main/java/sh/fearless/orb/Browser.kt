package sh.fearless.orb

import android.os.Bundle
import android.webkit.WebView
import sh.fearless.orb.adapters.ViewPageAdapter
import sh.fearless.orb.data.Tab
import sh.fearless.orb.fragments.OrbWebViewFragment

class Browser(
    private val getAdapter: () -> ViewPageAdapter
) {
//    var adapter: ViewPageAdapter? = null
//    var onPageTap: (() -> Unit)? = null
    private val adapter: ViewPageAdapter
        get() = getAdapter()

    val tabs = ArrayList<Tab>()
    var currentIndex = 0
    val currentTab: Tab
        get() = tabs[currentIndex]
    val currentWebView: WebView
        get() = currentTab.fragment.webView

    fun newTab(url: String) {
        val fragment = OrbWebViewFragment()
        val bundle = Bundle()
        bundle.putString("url", url)
        bundle.putBoolean("should_pause", tabs.size > 0)
        fragment.arguments = bundle
        tabs.add(Tab("blank:tab", fragment, url))
        adapter.notifyItemInserted(tabs.lastIndex)
    }
}