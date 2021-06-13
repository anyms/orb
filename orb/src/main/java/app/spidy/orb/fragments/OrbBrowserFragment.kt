package app.spidy.orb.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.spidy.kotlinutils.gone
import app.spidy.kotlinutils.invisible
import app.spidy.kotlinutils.isDarkThemeOn
import app.spidy.kotlinutils.visible
import app.spidy.orb.Browser
import app.spidy.orb.R
import app.spidy.orb.adapters.ViewPageAdapter
import app.spidy.orb.databinding.OrbFragmentBrowserBinding
import app.spidy.orb.interfaces.BrowserListener
import app.spidy.orb.utils.*


class OrbBrowserFragment : Fragment(), BrowserListener {
    private lateinit var binding: OrbFragmentBrowserBinding
    private lateinit var tabsItemDecoration: HorizontalMarginItemDecoration
    private lateinit var browser: Browser
    private lateinit var adapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OrbFragmentBrowserBinding.inflate(inflater, container, false)
        browser = Browser(
            getAdapter = { return@Browser adapter }
        )

        adapter = ViewPageAdapter(browser.tabs, childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 100
        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                browser.currentIndex = position
            }
        })
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val switchTo = viewHolder.adapterPosition - 1
                adapter.removeItem(viewHolder.adapterPosition)
                binding.viewPager.currentItem = if (switchTo < 0) 0 else switchTo
            }
        }).attachToRecyclerView(binding.viewPager.getRecyclerView())
        tabsItemDecoration = HorizontalMarginItemDecoration(requireContext(), R.dimen.orb_viewpager_current_item_horizontal_margin)

        binding.clearTabsBtn.setOnClickListener {

        }

        browser.newTab("https://youtube.com")
        browser.newTab("https://github.com")
        browser.newTab("https://google.com")


        /* setup toolbar */
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.orb_ic_home)
        requireActivity().title = ""

        updateTheme()

        return binding.root
    }

    private fun updateTheme() {
        if (requireContext().isDarkThemeOn()) {
            binding.appBarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orb_black))
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orb_black)
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        } else {
            binding.appBarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orb_white))
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orb_white)
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }
    }

    private fun switchToTabMode() {
        binding.clearTabsBtn.visible()
        binding.tabModeToolbar.visible()
        binding.appBarLayout.gone()

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = if (requireContext().isDarkThemeOn()) {
            ContextCompat.getColor(requireContext(), R.color.orb_light_black)
        } else {
            ContextCompat.getColor(requireContext(), R.color.orb_light_gray)
        }

        val nextItemVisiblePx = resources.getDimension(R.dimen.orb_viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.orb_viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        binding.viewPager.setPageTransformer(TabPageTransformer(pageTranslationX))
        binding.viewPager.addItemDecoration(tabsItemDecoration)
        binding.viewPager.isUserInputEnabled = true
        for (tab in browser.tabs) {
            tab.fragment.pauseWebView()
            tab.fragment.overlayView.visibility = View.VISIBLE
        }
    }

    fun resetTabMode() {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = if (requireContext().isDarkThemeOn()) {
            ContextCompat.getColor(requireContext(), R.color.orb_black)
        } else {
            ContextCompat.getColor(requireContext(), R.color.orb_white)
        }

        binding.tabModeToolbar.gone()
        binding.clearTabsBtn.gone()
        binding.appBarLayout.visible()
        binding.viewPager.setPageTransformer(ResetPageTransformer())
        binding.viewPager.removeItemDecoration(tabsItemDecoration)
        binding.viewPager.isUserInputEnabled = false
    }

    /* This is a workaround for the issue: https://issuetracker.google.com/issues/175796502?pli=1 */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val currentIndex = binding.viewPager.currentItem
            binding.viewPager.setCurrentItem(if (currentIndex-1 > 0) currentIndex-1 else 0, false)
            binding.viewPager.setCurrentItem(currentIndex, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear() // whenever I switch to dark theme the menu added again, so clear and add
        inflater.inflate(R.menu.orb_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuTabCount -> {
                switchToTabMode()
            }
        }
        return true
    }


    /* listener methods */

    override fun onNewTab(url: String) {
        browser.newTab(url)
    }

    override fun onProgressChanged(webView: WebView?, progress: Int) {
        if (browser.currentWebView == webView) {
            if (binding.progressBar.visibility == View.GONE) {
                binding.progressBar.visible()
            }
            binding.progressBar.progress = progress
        }
    }

    override fun onPageStarted(view: WebView?, url: String?) {
        if (browser.currentWebView == view) {
            binding.progressBar.visible()
            url?.also { binding.urlBar.setText(it) }
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (browser.currentWebView == view) {
            binding.progressBar.invisible()
            url?.also { binding.urlBar.setText(it) }
        }
    }
}