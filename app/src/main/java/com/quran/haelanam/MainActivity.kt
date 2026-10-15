package com.quran.haelanam

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {

    companion object {
        // الرابط المحلي التلقائي داخل التطبيق (يعمل بدون إنترنت وبأعلى سرعة)
        const val LOCAL_URL = "file:///android_asset/index.html"
        // رابط السيرفر/الموقع بعد الرفع
        const val CLOUD_URL = "https://ais-pre-lcyvlje5mim4gidc2stpib-425685275819.europe-west2.run.app"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AppWebView(url = LOCAL_URL)
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AppWebView(url: String) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // دعم الرجوع في سجل الصفحات عند الضغط على زر الرجوع في الهاتف
    BackHandler(enabled = webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                // تجنب خطأ MESA rendernode في المحاكي بتهيئة نمط العرض البرمجي
                try {
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                } catch (_: Throwable) {
                    setLayerType(View.LAYER_TYPE_NONE, null)
                }

                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: WebView?,
                        errorCode: Int,
                        description: String?,
                        failingUrl: String?
                    ) {
                        // في حال تعذر الاتصال بالرابط الخارجي يتم التوجيه التلقائي للملفات المحلية
                        if (failingUrl != MainActivity.LOCAL_URL) {
                            view?.loadUrl(MainActivity.LOCAL_URL)
                        }
                    }

                    // معالجة استقرار محرك الرندرة لمنع أي انهيار للواجهة
                    override fun onRenderProcessGone(
                        view: WebView?,
                        detail: RenderProcessGoneDetail?
                    ): Boolean {
                        view?.loadUrl(MainActivity.LOCAL_URL)
                        return true
                    }
                }
                webChromeClient = WebChromeClient()
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    allowFileAccessFromFileURLs = true
                    allowUniversalAccessFromFileURLs = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    textZoom = 100
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                }
                webViewInstance = this
                loadUrl(url)
            }
        },
        update = { webView ->
            webViewInstance = webView
        }
    )
}
