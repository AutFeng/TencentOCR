package com.project.demo.utils

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * HTTP网络请求工具类
 * 使用HttpURLConnection实现网络请求
 */
object HttpUtil {

    private const val TAG = "HttpUtil"
    private const val CONNECT_TIMEOUT = 15000
    private const val READ_TIMEOUT = 15000

    /**
     * 发送POST请求
     * @param urlString 请求URL
     * @param headers 请求头
     * @param body 请求体
     * @return 响应字符串
     */
    fun post(urlString: String, headers: Map<String, String>, body: String): String {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection

            // 设置请求方法和超时
            connection.requestMethod = "POST"
            connection.connectTimeout = CONNECT_TIMEOUT
            connection.readTimeout = READ_TIMEOUT
            connection.doOutput = true
            connection.doInput = true

            // 设置请求头
            headers.forEach { (key, value) ->
                connection.setRequestProperty(key, value)
            }

            // 写入请求体
            OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { writer ->
                writer.write(body)
                writer.flush()
            }

            // 读取响应
            val responseCode = connection.responseCode
            Log.d(TAG, "Response Code: $responseCode")

            val inputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                reader.readText()
            }

            Log.d(TAG, "Response: $response")
            return response

        } catch (e: Exception) {
            Log.e(TAG, "Request failed", e)
            throw e
        } finally {
            connection?.disconnect()
        }
    }
}
