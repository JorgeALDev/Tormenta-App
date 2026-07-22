package com.T20.tormentaapp.ui.fichas

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentFichasBinding

class FichasListFragment : Fragment(R.layout.fragment_fichas) {

    private val url = "https://fichasdenimb.com.br"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFichasBinding.bind(view)
        val webview = binding.webview
        val prefs = requireContext().getSharedPreferences("progresso_webview", Context.MODE_PRIVATE)

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = false

            override fun onPageFinished(view: WebView?, pageUrl: String?) {
                super.onPageFinished(view, pageUrl)
                if (pageUrl != null) prefs.edit().putString(url, pageUrl).apply()
            }
        }

        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.loadsImagesAutomatically = true
        webview.settings.allowFileAccess = true
        webview.settings.allowContentAccess = true
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true

        val urlSalva = prefs.getString(url, null)
        webview.loadUrl(urlSalva ?: url)

        binding.btnLimparFichas.setOnClickListener {
            webview.clearCache(true)
            webview.clearHistory()
            WebStorage.getInstance().deleteAllData()
            CookieManager.getInstance().removeAllCookies(null)
            prefs.edit().remove(url).apply()
            webview.loadUrl(url)
        }
    }
}