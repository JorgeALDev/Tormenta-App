package com.T20.tormentaapp.ui.livros

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentLivroWebviewBinding
import com.T20.tormentaapp.util.WebViewConfigurador
import com.T20.tormentaapp.util.WebViewNavigationGuard

class LivroWebViewFragment : Fragment(R.layout.fragment_livro_webview) {

    private val url: String by lazy { requireArguments().getString("url", "") }

    companion object {
        fun newInstance(url: String): LivroWebViewFragment {
            return LivroWebViewFragment().apply {
                arguments = Bundle().apply { putString("url", url) }
            }
        }

        private const val JS_OCULTAR_ANUNCIOS = """
(function() {
    function removerSobrepostos() {
        var fixos = document.querySelectorAll('[style*="position: fixed"][style*="bottom"], [style*="position: sticky"][style*="bottom"]');
        fixos.forEach(function(el) {
            if (!el.id || !el.id.includes('flipbook')) {
                el.remove();
            }
        });

        var overlays = document.querySelectorAll('[style*="position: fixed"][style*="z-index"], [style*="position: absolute"][style*="z-index"]');
        overlays.forEach(function(el) {
            var bg = window.getComputedStyle(el).backgroundColor;
            var isOverlay = bg.includes('rgba(0,0,0,') || bg.includes('#000') || bg.includes('black') || bg.includes('white');
            if (isOverlay && el.offsetHeight > 100 && el.offsetWidth > 100) {
                el.remove();
            }
        });

        var popups = document.querySelectorAll('[class*="modal"], [class*="popup"], [class*="overlay"], [id*="modal"], [id*="popup"], [id*="overlay"]');
        popups.forEach(function(el) {
            if (!el.closest('#flipbook') && !el.closest('.flipbook')) {
                el.remove();
            }
        });

        var banners = document.querySelectorAll('div[style*="height: 60px"], div[style*="height: 90px"], div[style*="height: 250px"]');
        banners.forEach(function(el) {
            if (el.offsetParent !== null && !el.closest('#flipbook')) {
                el.remove();
            }
        });

        var iframes = document.querySelectorAll('iframe[src*="doubleclick"], iframe[src*="googlesyndication"], iframe[src*="google.com/pagead"]');
        iframes.forEach(function(el) {
            el.remove();
        });
    }

    removerSobrepostos();

    var observer = new MutationObserver(function() {
        removerSobrepostos();
    });
    observer.observe(document.body, { childList: true, subtree: true });

    setTimeout(removerSobrepostos, 2000);
    setTimeout(removerSobrepostos, 5000);
})();
"""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLivroWebviewBinding.bind(view)
        val webview = binding.webview
        val prefs = requireContext().getSharedPreferences("progresso_webview", Context.MODE_PRIVATE)

        WebViewConfigurador.aplicarConfiguracaoPadrao(webview)

        val prefixo = prefixoDoLivro(url)
        val guard = WebViewNavigationGuard(webview, url, prefixo)

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val novaUrl = request?.url.toString()
                if (!novaUrl.startsWith(prefixo)) {
                    guard.verificarEAvisar(novaUrl, binding.root)
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView?, pageUrl: String?) {
                super.onPageFinished(view, pageUrl)
                if (pageUrl != null) {
                    prefs.edit().putString(url, pageUrl).apply()
                }
                if (pageUrl != null && !pageUrl.startsWith(prefixo)) {
                    guard.verificarEAvisar(pageUrl, binding.root)
                }
                view?.evaluateJavascript(JS_OCULTAR_ANUNCIOS, null)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url.toString()
                val blockedDomains = listOf(
                    "doubleclick.net",
                    "googlesyndication.com",
                    "googleadservices.com",
                    "google.com/pagead",
                    "adservice.google",
                    "partner.googleadservices",
                    "amazon-adsystem.com",
                    "criteo.com"
                )
                if (blockedDomains.any { url.contains(it, ignoreCase = true) }) {
                    return WebResourceResponse("text/plain", "utf-8", null)
                }
                return super.shouldInterceptRequest(view, request)
            }
        }

        val urlSalva = prefs.getString(url, null)
        webview.loadUrl(urlSalva ?: url)
    }

    private fun prefixoDoLivro(urlOriginal: String): String {
        val uri = Uri.parse(urlOriginal)
        val idDoLivro = uri.pathSegments.take(2).joinToString("/")
        return "${uri.scheme}://${uri.host}/$idDoLivro"
    }
}