package com.T20.tormentaapp.ui.fichas

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentFichasBinding
import com.T20.tormentaapp.util.*

class FichasListFragment : Fragment(R.layout.fragment_fichas) {

    private val url = "https://fichasdenimb.com.br"
    private lateinit var popupHandler: PopupLoginHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFichasBinding.bind(view)
        val webview = binding.webview
        val prefs = requireContext().getSharedPreferences("progresso_webview", Context.MODE_PRIVATE)

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webview, true)
        }
        WebViewConfigurador.aplicarConfiguracaoPadrao(webview)
        webview.settings.setSupportMultipleWindows(true)

        webview.addJavascriptInterface(BlobDownloadInterface(requireContext()), "AndroidBlobDownload")


        popupHandler = PopupLoginHandler(
            popupContainer = binding.popupContainer,
            popupWebView = binding.popupWebview,
            dominioPrincipal = url,
            onLoginConcluido = { webview.reload() }
        )

        webview.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                if (popupHandler.criarWebChromeClient().onCreateWindow(view, isDialog, isUserGesture, resultMsg)) {
                    return true
                }

                val transport = resultMsg?.obj as? WebView.WebViewTransport
                transport?.let {
                    val newWebView = WebView(requireContext())
                    newWebView.settings.javaScriptEnabled = true
                    newWebView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            if (url != null && (url.endsWith(".pdf") || url.contains("download"))) {
                                DownloadHelper.baixarPdf(requireContext(), url)
                                view?.loadUrl("about:blank")
                            }
                        }
                    }
                    it.webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
                return false
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebViewConsole", consoleMessage.message())
                return super.onConsoleMessage(consoleMessage)
            }
        }

        val guard = WebViewNavigationGuard(webview, url, url)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = false

            override fun onPageFinished(view: WebView?, pageUrl: String?) {
                super.onPageFinished(view, pageUrl)
                if (pageUrl != null) {
                    prefs.edit().putString(url, pageUrl).apply()
                    guard.verificarEAvisar(pageUrl, binding.root)
                }

                view?.evaluateJavascript(JavaScriptConstants.CAPTURAR_BLOB, null)
                view?.postDelayed({ popupHandler.injetarCorrecaoDeLogin(view) }, 1500)
            }
        }

        webview.setDownloadListener { downloadUrl, _, _, _, _ ->
            if (downloadUrl.startsWith("data:") || downloadUrl.startsWith("blob:")) {
                Log.w("Download", "URL não suportada diretamente: $downloadUrl")
            } else {
                DownloadHelper.baixarPdf(requireContext(), downloadUrl)
            }
        }

        val urlSalva = prefs.getString(url, null)
        webview.loadUrl(urlSalva ?: url)

        binding.btnFecharPopup.setOnClickListener { popupHandler.fecharPopup() }
    }
}