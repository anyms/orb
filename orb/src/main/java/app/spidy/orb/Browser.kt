package app.spidy.orb

import app.spidy.orb.adapters.ViewPageAdapter
import app.spidy.orb.data.Tab
import app.spidy.orb.fragments.WebViewFragment

class Browser(private val getAdapter: () -> ViewPageAdapter) {
    private val adapter: ViewPageAdapter
        get() = getAdapter()

    val tabs = ArrayList<Tab>()

    fun newTab(url: String) {
        val fragment = WebViewFragment()
        tabs.add(Tab("blank:tab", fragment))
        adapter.notifyItemInserted(tabs.lastIndex)
    }
}