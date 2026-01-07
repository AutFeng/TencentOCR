package com.project.demo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * 图片处理工具类
 * 提供图片压缩和Base64编码功能
 */
object ImageUtil {

    /**
     * 将Uri转换为Base64编码字符串
     * @param context 上下文
     * @param uri 图片Uri
     * @param maxSize 最大尺寸（宽或高）
     * @return Base64编码字符串
     */
    fun uriToBase64(context: Context, uri: Uri, maxSize: Int = 1024): String {
        val bitmap = uriToBitmap(context, uri, maxSize)
        return bitmapToBase64(bitmap)
    }

    /**
     * 将Uri转换为Bitmap
     */
    private fun uriToBitmap(context: Context, uri: Uri, maxSize: Int): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        // 计算缩放比例
        var scale = 1
        while (options.outWidth / scale > maxSize || options.outHeight / scale > maxSize) {
            scale *= 2
        }

        // 解码图片
        val inputStream2 = context.contentResolver.openInputStream(uri)
        val options2 = BitmapFactory.Options().apply {
            inSampleSize = scale
        }
        val bitmap = BitmapFactory.decodeStream(inputStream2, null, options2)
        inputStream2?.close()

        return bitmap ?: throw IllegalArgumentException("无法解码图片")
    }

    /**
     * 将Bitmap转换为Base64编码字符串
     */
    private fun bitmapToBase64(bitmap: Bitmap, quality: Int = 85): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
