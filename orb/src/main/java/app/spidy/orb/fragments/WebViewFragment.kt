package app.spidy.orb.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import app.spidy.kotlinutils.toast
import app.spidy.orb.R
import app.spidy.orb.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private lateinit var binding: FragmentWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)

        binding.webView.settings.apply {
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            useWideViewPort = true
            setSupportMultipleWindows(true)
            builtInZoomControls = true
            loadWithOverviewMode = true
            supportZoom()
            displayZoomControls = false
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setNeedInitialFocus(true)
        }
        binding.webView.isFocusableInTouchMode = true
        binding.webView.isFocusable = true
        binding.webView.requestFocus()

        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()

        binding.webView.loadUrl(requireArguments().getString("url")!!)

        binding.overlayView.setOnClickListener {
            requireContext().toast("Touched!")
            binding.overlayView.visibility = View.GONE
        }

        return binding.root
    }
}