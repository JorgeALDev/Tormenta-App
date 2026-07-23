package com.T20.tormentaapp.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object DownloadHelper {

    fun baixarPdf(context: Context, url: String) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Toast.makeText(context, "Erro ao baixar: ${response.code}", Toast.LENGTH_LONG).show()
                return
            }

            val fileName = "ficha_${System.currentTimeMillis()}.pdf"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                } else {
                    put(MediaStore.MediaColumns.DATA, "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$fileName")
                }
            }

            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    response.body?.byteStream()?.use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Toast.makeText(context, "PDF salvo em Downloads", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Erro ao salvar arquivo", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Erro de rede: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } catch (e: Exception) {
            Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun salvarBase64ComoPdf(context: Context, base64Data: String, fileName: String) {
        try {
            val bytes = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                } else {
                    put(MediaStore.MediaColumns.DATA, "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$fileName")
                }
            }
            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(bytes)
                }
                Toast.makeText(context, "PDF salvo: $fileName", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Erro ao salvar PDF", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}