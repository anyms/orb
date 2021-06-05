package app.spidy.orb.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.spidy.kotlinutils.onUiThread
import app.spidy.orb.Browser
import app.spidy.orb.R
import app.spidy.orb.adapters.ViewPageAdapter
import app.spidy.orb.data.Tab
import app.spidy.orb.databinding.FragmentBrowserBinding
import app.spidy.orb.utils.HorizontalMarginItemDecoration
import app.spidy.orb.utils.ResetMarginItemDecoration
import app.spidy.orb.utils.ResetPageTransformer
import app.spidy.orb.utils.TabPageTransformer
import kotlin.concurrent.thread

class BrowserFragment : Fragment() {
    private lateinit var binding: FragmentBrowserBinding
    private lateinit var tabsItemDecoration: HorizontalMarginItemDecoration
    private lateinit var browser: Browser
    private lateinit var adapter: ViewPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBrowserBinding.inflate(inflater, container, false)
        browser = Browser(
            getAdapter = { return@Browser adapter }
        )

        adapter = ViewPageAdapter(browser.tabs, requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 100
        binding.viewPager.clipToPadding = false
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }).attachToRecyclerView(binding.viewPager)

        tabsItemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        resetTabMode()
        browser.newTab("https://google.com")
        browser.newTab("https://google.com")
        browser.newTab("https://google.com")
        switchToTabMode()

        return binding.root
    }

    fun getBrowser() = browser

    private fun switchToTabMode() {
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        binding.viewPager.setPageTransformer(TabPageTransformer(pageTranslationX))
        binding.viewPager.addItemDecoration(tabsItemDecoration)
        binding.viewPager.isUserInputEnabled = true
    }

    private fun resetTabMode() {
        binding.viewPager.setPageTransformer(ResetPageTransformer())
        binding.viewPager.removeItemDecoration(tabsItemDecoration)
        binding.viewPager.isUserInputEnabled = false
    }
}