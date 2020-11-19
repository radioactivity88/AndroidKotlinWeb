package com.radioactivity.test.androidkotlinweb

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        const val URL_ADDRESS = "file:///android_asset/index.html"
    }

    private lateinit var btnRefresh: ImageButton
    private lateinit var btnAdd: ImageButton
    private lateinit var btnRandom:ImageButton
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
        btnRandom = findViewById(R.id.button_random)
        textNotify = findViewById(R.id.text_notify)
        webView = findViewById(R.id.web_view)

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(JsHandler(this, textNotify), "PosApi")
        webView.settings.javaScriptEnabled = true

        val handler = WebViewHandler(webView)
        btnAdd.setOnClickListener { handler.add() }
        btnClean.setOnClickListener { handler.clean() }
        btnRefresh.setOnClickListener { handler.refresh() }
        btnRandom.setOnClickListener { handler.addRandom() }

        // load page on start
        handler.refresh()
    }

    /**
     * This handler catches page status
     */
    private class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            view?.loadUrl("javascript:startup()")
        }
    }

    /**
     * This is handler describes API of JS scrip.
     * Android application can call this methods to send some data into JS
     */
    class WebViewHandler(private val ww: WebView) {
        fun add() {
            ww.loadUrl("javascript:add()")
        }

        fun addRandom(){
            val random = Random(Date().time)
            ww.loadUrl("javascript:random(" + abs(random.nextInt() % 100) + ")")
        }

        fun clean() {
            ww.loadUrl("javascript:clean()")
        }

        fun refresh() {
            ww.loadUrl(URL_ADDRESS)
        }

    }

    /**
     * This is API for JS, javascript can call methods annotated by  @JavascriptInterface
     * Be carefully with parameters! See JS->Java and Java->JS compatibility.
     */
    class JsHandler(private val activity: Activity, private val textInfo: EditText) {

        @JavascriptInterface
        fun notify(message: String) {
            activity.runOnUiThread { textInfo.setText(message.trim()) }
        }
    }

}