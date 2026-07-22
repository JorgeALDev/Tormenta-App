package com.T20.tormentaapp.ui.fichas

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentLivroWebviewBinding

class FichasListFragment : Fragment(R.layout.fragment_livro_webview) {

    private val url = "https://fichasdenimb.com.br"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLivroWebviewBinding.bind(view)
        val webview = binding.webview
        val prefs = requireContext().getSharedPreferences("progresso_webview", Context.MODE_PRIVATE)

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
    }
}