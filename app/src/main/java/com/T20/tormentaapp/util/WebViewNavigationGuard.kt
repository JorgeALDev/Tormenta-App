package com.T20.tormentaapp.util

import android.view.View
import android.webkit.WebView
import com.google.android.material.snackbar.Snackbar

class WebViewNavigationGuard(
    private val webview: WebView,
    private val urlPrincipal: String,
    private val prefixoValido: String
) {
    fun verificarEAvisar(urlAtual: String?, view: View) {
        if (urlAtual == null || urlAtual.startsWith(prefixoValido)) return

        Snackbar.make(view, "Você não pode sair da página do livro", Snackbar.LENGTH_LONG)
            .setAction("") { webview.loadUrl(urlPrincipal) }
            .show()
    }
}