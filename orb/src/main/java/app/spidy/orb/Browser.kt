package app.spidy.orb

import android.os.Bundle
import app.spidy.orb.adapters.ViewPageAdapter
import app.spidy.orb.data.Tab
import app.spidy.orb.fragments.WebViewFragment

class Browser(private val getAdapter: () -> ViewPageAdapter) {
    private val adapter: ViewPageAdapter
        get() = getAdapter()

    val tabs = ArrayList<Tab>()
    var currentIndex = 0

    fun newTab(url: String) {
        val fragment = WebViewFragment()
        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle
        tabs.add(Tab("blank:tab", fragment, url))
        adapter.notifyItemInserted(tabs.lastIndex)
    }
}