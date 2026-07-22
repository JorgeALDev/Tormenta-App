package com.T20.tormentaapp.util

import android.webkit.WebView

object WebViewConfigurador {

    fun aplicarConfiguracaoPadrao(webview: WebView) {
        webview.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadsImagesAutomatically = true
            allowFileAccess = true
            allowContentAccess = true
            useWideViewPort = true
            loadWithOverviewMode = true
        }
    }
}