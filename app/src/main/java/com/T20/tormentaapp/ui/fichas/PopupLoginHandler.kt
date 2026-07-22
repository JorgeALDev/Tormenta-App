package com.T20.tormentaapp.ui.fichas

import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.T20.tormentaapp.util.WebViewConfigurador


class PopupLoginHandler(
    private val popupContainer: View,
    private val popupWebView: WebView,
    private val dominioPrincipal: String,
    private val onLoginConcluido: () -> Unit
) {

    fun criarWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                resultMsg?.let { mostrarPopup(it) }
                return true
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("PopupLoginHandler", consoleMessage.message())
                return super.onConsoleMessage(consoleMessage)
            }
        }
    }

    fun injetarCorrecaoDeLogin(view: WebView?) {
        val js = """
            (function() {
                try {
                    if (typeof firebase !== 'undefined' && firebase.auth && firebase.auth.constructor?.prototype) {
                        var proto = firebase.auth.constructor.prototype;
                        if (!proto._popupPatched) {
                            proto._popupPatched = true;
                            proto.signInWithRedirect = function(provider) {
                                return this.signInWithPopup(provider);
                            };
                        }
                    }
                } catch (e) {
                    console.error('Falha ao ajustar login: ' + e.message);
                }
            })();
        """.trimIndent()
        view?.evaluateJavascript(js, null)
    }

    private fun mostrarPopup(resultMsg: Message) {
        WebViewConfigurador.aplicarConfiguracaoPadrao(popupWebView)

        popupWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null && url.startsWith(dominioPrincipal)) {
                    fecharPopup()
                    onLoginConcluido()
                }
            }
        }

        val transport = resultMsg.obj as? WebView.WebViewTransport
        transport?.webView = popupWebView
        resultMsg.sendToTarget()

        popupContainer.visibility = View.VISIBLE
    }

    fun fecharPopup() {
        popupContainer.visibility = View.GONE
        popupWebView.stopLoading()
        popupWebView.loadUrl("about:blank")
    }
}