package com.example.bookinglapangan.ui.payment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.api.RetrofitClient
import kotlinx.coroutines.launch

class XenditWebViewActivity : ComponentActivity() {

    companion object {
        const val EXTRA_INVOICE_URL = "invoice_url"
        const val EXTRA_EXTERNAL_ID = "external_id"

        //Pastikan path ini SAMA dengan success_redirect_url / failure_redirect_url di backend
        private const val SUCCESS_PATH = "/payment-success"
        private const val FAILED_PATH  = "/payment-failed"

        private const val TAG = "XENDIT_WEBVIEW"
    }

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_payment)

        val invoiceUrl = intent.getStringExtra(EXTRA_INVOICE_URL)
        val externalId = intent.getStringExtra(EXTRA_EXTERNAL_ID)

        Log.d("PAY", "Start Xendit (WebViewActivity): url=$invoiceUrl, ext=$externalId")

        if (invoiceUrl.isNullOrBlank() || externalId.isNullOrBlank()) {
            Log.e(TAG, "invoiceUrl / externalId kosong")
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        val tvClose = findViewById<TextView>(R.id.tvClose)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        progress = findViewById(R.id.progress)
        webView = findViewById(R.id.webView)

        tvTitle.text = "Pembayaran"
        tvClose.setOnClickListener { finish() }

        // --- WebView settings
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)         // penting untuk popup/3DS
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            userAgentString = userAgentString + " BookingApp/1.0"
        }

        // Layer type: emulator kadang rewel; coba hardware, fallback ke software jika perlu
        try { webView.setLayerType(View.LAYER_TYPE_HARDWARE, null) } catch (_: Exception) {}

        // Cookie: beberapa wallet/redirect perlu
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
                progress.progress = newProgress
            }

            // Penting: handle window baru (target=_blank / 3DS)
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message?
            ): Boolean {
                val transport = resultMsg?.obj as? WebView.WebViewTransport ?: return false
                val child = WebView(this@XenditWebViewActivity).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val url = request?.url?.toString().orEmpty()
                            Log.d(TAG, "[child] nav: $url")
                            if (handleExternalScheme(url)) return true
                            return false
                        }
                    }
                }
                transport.webView = child
                resultMsg?.sendToTarget()
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString().orEmpty()
                Log.d(TAG, "navigate: $url")

                // 1) tangani success/failed redirect → cek status via backend
                //    disarankan cek juga host agar tidak false positive
                val uri = Uri.parse(url)
                val isSameHost = try {
                    // ganti ke domain kamu, mis. book.deltagrup.id
                    uri.host?.contains("deltagrup.id", ignoreCase = true) == true
                } catch (_: Exception) { false }

                if (isSameHost && (uri.path?.contains(SUCCESS_PATH) == true || uri.path?.contains(FAILED_PATH) == true)) {
                    checkStatusAndFinish(externalId)
                    return true
                }

                // 2) tangani skema non http(s) (intent://, ovo://, dana://, gojek://, shopeeid://, linkaja://, market://)
                if (handleExternalScheme(url)) return true

                // 3) default: biarkan WebView memuat
                return false
            }

            // Optional: visibilitas progress jika halaman lambat
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progress.visibility = View.GONE
            }
        }

        Log.d(TAG, "load invoice: $invoiceUrl (externalId=$externalId)")
        webView.loadUrl(invoiceUrl)
    }

    private fun handleExternalScheme(url: String): Boolean {
        val lower = url.lowercase()
        val isHttp = lower.startsWith("http://") || lower.startsWith("https://")
        if (isHttp) return false

        // intent:// — parse ke Intent
        if (lower.startsWith("intent://")) {
            return try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                if (intent.`package` != null) {
                    startActivity(intent)
                } else {
                    val fallback = intent.getStringExtra("browser_fallback_url")
                    if (!fallback.isNullOrBlank()) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fallback)))
                    }
                }
                true
            } catch (_: Exception) { true }
        }

        // skema e-wallet lain
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            true
        } catch (_: ActivityNotFoundException) {
            // fallback Play Store per aplikasi umum
            val pkg = when {
                lower.startsWith("ovo://") -> "ovo.id"
                lower.startsWith("dana://") -> "id.dana"
                lower.startsWith("gojek://") -> "com.gojek.app"
                lower.startsWith("shopeeid://") -> "com.shopee.id"
                lower.startsWith("linkaja://") -> "com.telkom.mwallet"
                else -> null
            }
            if (pkg != null) {
                return try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
                    true
                } catch (_: Exception) { true }
            }
            true
        }
    }

    private fun checkStatusAndFinish(externalId: String) {
        lifecycleScope.launch {
            try {
                val res = RetrofitClient.apiService.getPaymentStatus(externalId)
                val status = if (res.isSuccessful) res.body()?.status ?: "UNKNOWN" else "UNKNOWN"
                val data = Intent().apply {
                    putExtra("status", status)
                    putExtra("external_id", externalId)
                }
                setResult(RESULT_OK, data)
            } catch (e: Exception) {
                Log.e(TAG, "checkStatus error: ${e.message}")
                setResult(RESULT_CANCELED)
            }
            finish()
        }
    }

    override fun onDestroy() {
        // bersihkan WebView (hindari leak)
        try {
            webView.apply {
                loadUrl("about:blank")
                clearHistory()
                removeAllViews()
                destroy()
            }
        } catch (_: Exception) {}
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
