package com.T20.tormentaapp.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast

object DownloadHelper {

    fun baixarPdf(context: Context, url: String) {
        try {
            val nomeArquivo = "ficha_${System.currentTimeMillis()}.pdf"
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle(nomeArquivo)
                setMimeType("application/pdf")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nomeArquivo)
            }
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            Toast.makeText(context, "Baixando ficha...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao baixar arquivo", Toast.LENGTH_SHORT).show()
        }
    }
}