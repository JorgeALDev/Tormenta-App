package com.T20.tormentaapp.util

import android.content.Context
import android.webkit.JavascriptInterface
import android.util.Log

class BlobDownloadInterface(private val context: Context) {

    @JavascriptInterface
    fun salvarPDF(base64Data: String, fileName: String) {
        Log.d("BlobDownload", "Recebendo PDF: $fileName")
        DownloadHelper.salvarBase64ComoPdf(context, base64Data, fileName)
    }

    @JavascriptInterface
    fun salvarJSON(base64Data: String, fileName: String) {
        Log.d("BlobDownload", "Recebendo JSON: $fileName")

        try {
            val bytes = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            val jsonString = String(bytes)

            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/json")
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                } else {
                    put(android.provider.MediaStore.MediaColumns.DATA, "${android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)}/$fileName")
                }
            }
            val uri = context.contentResolver.insert(android.provider.MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(bytes)
                }
                android.widget.Toast.makeText(context, "JSON salvo: $fileName", android.widget.Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "Erro ao salvar JSON", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}