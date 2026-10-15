package com.quran.haelanam

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppWebView(url = LOCAL_URL) // استبدل الرابط برابط موقعك/سيرفرك بعد الرفع أو احتفظ بالأصول المحلية
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
                }
                webChromeClient = WebChromeClient()
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    useWideViewPort = true
                    loadWithOverviewMode = true
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
