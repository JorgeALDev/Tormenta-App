package com.T20.tormentaapp.ui.livros

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentLivroWebviewBinding
import kotlin.getValue

class LivroWebViewFragment : Fragment(R.layout.fragment_livro_webview) {

    private val args: LivroWebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLivroWebviewBinding.bind(view)
        val webview = binding.webview

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
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

        webview.loadUrl(args.url)
    }
}