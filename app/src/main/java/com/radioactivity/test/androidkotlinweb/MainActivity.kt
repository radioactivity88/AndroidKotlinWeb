package com.radioactivity.test.androidkotlinweb

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val URL_ADDRESS = "https://google.com"
    }

    private lateinit var btnRefresh: ImageButton
    private lateinit var btnAdd: ImageButton
    private lateinit var btnClean: ImageButton
    private lateinit var textNotify: EditText
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRefresh = findViewById(R.id.button_update)
        btnAdd = findViewById(R.id.button_add)
        btnClean = findViewById(R.id.button_clean)
        textNotify = findViewById(R.id.text_notify);
        webView = findViewById(R.id.web_view)

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(JsHandler(textNotify), "PosApi")
        webView.settings.javaScriptEnabled = true

        val handler = WebViewHandler(webView)
        btnAdd.setOnClickListener { handler.add() }
        btnClean.setOnClickListener { handler.clean() }
        btnRefresh.setOnClickListener { handler.refresh() }
    }

    private class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            view?.loadUrl("javascript:startup()")
        }
    }

    class WebViewHandler(private val ww: WebView) {
        fun add() {
            ww.loadUrl("javascript:add()")
        }

        fun clean() {
            ww.loadUrl("javascript:clean()")
        }

        fun refresh() {
            ww.loadUrl(URL_ADDRESS)
        }

    }


    class JsHandler(private val textInfo: EditText) {
        @JavascriptInterface
        fun notify(message: String) {
            textInfo.setText(message)
        }
    }

}