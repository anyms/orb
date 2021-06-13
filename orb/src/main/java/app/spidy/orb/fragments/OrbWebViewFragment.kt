package app.spidy.orb.fragments

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import app.spidy.kotlinutils.debug
import app.spidy.kotlinutils.onUiThread
import app.spidy.orb.R
import app.spidy.orb.databinding.OrbFragmentWebViewBinding
import app.spidy.orb.interfaces.BrowserListener
import app.spidy.orb.interfaces.WebViewEventListener
import app.spidy.orb.webclients.ChromeClient
import app.spidy.orb.webclients.WebClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.concurrent.thread

class OrbWebViewFragment : Fragment(), WebViewEventListener {
    private lateinit var binding: OrbFragmentWebViewBinding
    private var listener: BrowserListener? = null
    val overlayView: FrameLayout
        get() = binding.overlayView
    val webView: WebView
        get() = binding.webView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BrowserListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OrbFragmentWebViewBinding.inflate(inflater, container, false)
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
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webView.isFocusableInTouchMode = true
        binding.webView.isFocusable = true
        binding.webView.requestFocus()

        binding.webView.webViewClient = WebClient(this)
        binding.webView.webChromeClient = ChromeClient(requireContext(), this)
        binding.webView.loadUrl(requireArguments().getString("url")!!)

        binding.overlayView.setOnClickListener {
            listener?.onPageTap()
            binding.overlayView.visibility = View.GONE
            resumeWebView()
        }

        if (requireArguments().getBoolean("should_pause")) {
            pauseWebView()
        }

        configureContextMenu() // Configure context menu

        return binding.root
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun configureContextMenu() {
        var itemInfo: Bundle? = null
        val handler = Handler(Looper.getMainLooper()) {
            itemInfo = it.data
            return@Handler true
        }
        webView.setOnLongClickListener {

            registerForContextMenu(webView)

            val hr = (it as WebView).hitTestResult
            val message = handler.obtainMessage()
            val menu = ArrayList<CharSequence>()
            val callbacks = ArrayList<() -> Unit>()
            var title: String? = null

            if (hr.type == WebView.HitTestResult.SRC_ANCHOR_TYPE || hr.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                webView.requestFocusNodeHref(message)
                title = webView.title
                menu.add("Open in new tab")
                callbacks.add {
                    listener?.onNewTab(itemInfo?.get("url").toString())
                }
                menu.add("Copy link address")
                callbacks.add {
                    val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = android.content.ClipData
                        .newPlainText("Link address", itemInfo?.get("url").toString())
                    clipboardManager.setPrimaryClip(clipData)
                    Snackbar.make(webView, "Link address copied.", Snackbar.LENGTH_SHORT).show()
                }

                if (hr.type != WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    menu.add("Copy link text")
                    callbacks.add {
                        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = android.content.ClipData
                            .newPlainText("Link text", itemInfo?.get("title").toString())
                        clipboardManager.setPrimaryClip(clipData)

                        Snackbar.make(webView, "Link content copied.", Snackbar.LENGTH_SHORT).show()
                    }
                }

                menu.add("Share link")
                callbacks.add {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    if (itemInfo?.get("title") != null) {
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, itemInfo?.get("title").toString())
                    }
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, itemInfo?.get("url").toString())
                    startActivity(Intent.createChooser(sharingIntent, "Share via"))
                }
            }


            if (hr.type == WebView.HitTestResult.IMAGE_TYPE || hr.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                menu.add("Download image")
                callbacks.add {
//                    PermissionHandler.requestStorage(requireContext(), "To download the following image, the browser require storage permission. \n\n Would you like to grant?") {
//                        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
//                        DownloadHandler.download(context!!, hr.extra, webView!!, "")
//                    }
                }

                menu.add("Open image in new tab")

                title = hr.extra

                callbacks.add {
                    if (hr.extra != null) {
                        listener?.onNewTab(hr.extra!!.toString())
                    }
                }
                menu.add("Copy image address")
                callbacks.add {
                    if (hr.extra != null) {
                        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = android.content.ClipData
                            .newPlainText("Image link", hr.extra)
                        clipboardManager.setPrimaryClip(clipData)

                        Snackbar.make(webView, "Image address copied.", Snackbar.LENGTH_SHORT).show()
                    }
                }

                menu.add("Share image")
                callbacks.add {
                    if (hr.extra != null) {
                        Snackbar.make(webView, "Fetching image...", Snackbar.LENGTH_SHORT).show()
                        thread {
                            val file = File(requireContext().externalCacheDir, "share.png")
                            val fOut = FileOutputStream(file)
                            val imageUrl = hr.extra?.toString()
                            if (imageUrl!!.startsWith("data:")) {
                                val base64EncodedString = imageUrl.substring(imageUrl.indexOf(",") + 1)
                                fOut.write(Base64.decode(base64EncodedString, Base64.DEFAULT))
                            } else {
                                val u = URL(imageUrl)
                                val bitmap = BitmapFactory.decodeStream(u.openConnection().getInputStream())
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                            }

                            fOut.flush()
                            fOut.close()

                            onUiThread {
                                val sharingIntent = Intent(Intent.ACTION_SEND)
                                sharingIntent.type = "image/*"
                                sharingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                                startActivity(Intent.createChooser(sharingIntent, "Share via"))
                            }
                        }
                    }
                }
            }


            if (menu.isNotEmpty()) {
                val builder = AlertDialog.Builder(requireContext())
                if (title != null) {
                    builder.setTitle(title)
                } else {
                    builder.setTitle(webView.url)
                }
                builder.setItems(menu.toArray(arrayOf())) { dialog, index ->
                    callbacks[index]()
                }
                builder.show()
            }

            return@setOnLongClickListener false

        }
    }

    fun pauseWebView() {
        debug("WebView", "onPause")
//        binding.rootView.setBackgroundResource(R.drawable.tab_radius_and_stroke)
        binding.cardView.apply {
            radius = resources.getDimension(R.dimen.orb_card_view_corner_radius)
            elevation = resources.getDimension(R.dimen.orb_card_view_elevation)
            (layoutParams as ViewGroup.MarginLayoutParams).setMargins(10, 30, 10, 30)
            requestLayout()
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(200)
            binding.webView.onPause()
        }
    }
    fun resumeWebView() {
        debug("WebView", "onResume")
//        binding.rootView.setBackgroundResource(0)
        binding.cardView.apply {
            radius = 0f
            elevation = 0f
            (layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, 0)
            requestLayout()
        }
        binding.webView.onResume()
    }


    /* WebView event listener redirect them to activity */

    override fun onNewTab(url: String) {
        listener?.onNewTab(url)
    }

    override fun onProgressChanged(view: WebView?, progress: Int) {
        listener?.onProgressChanged(webView, progress)
    }

    override fun onPageStarted(view: WebView?, url: String?) {
        listener?.onPageStarted(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        listener?.onPageFinished(view, url)
    }
}