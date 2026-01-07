package com.project.demo.utils

import android.util.Log
import com.project.demo.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * 腾讯云OCR服务类
 * 封装身份证识别API调用
 */
object TencentOCRService {

    private const val TAG = "TencentOCRService"

    // 从 BuildConfig 读取密钥(密钥存储在 local.properties 中)
    private val SECRET_ID = BuildConfig.TENCENT_SECRET_ID
    private val SECRET_KEY = BuildConfig.TENCENT_SECRET_KEY

    private const val SERVICE = "ocr"
    private const val HOST = "ocr.tencentcloudapi.com"
    private const val ACTION = "IDCardOCR"
    private const val ENDPOINT = "https://$HOST"

    /**
     * 识别身份证
     * @param imageBase64 身份证图片的Base64编码
     * @param cardSide 身份证正反面，FRONT-正面，BACK-反面
     * @return 识别结果JSON字符串
     */
    suspend fun recognizeIDCard(imageBase64: String, cardSide: String = "FRONT"): String {
        return withContext(Dispatchers.IO) {
            try {
                val timestamp = System.currentTimeMillis() / 1000

                // 构建请求体
                val payload = JSONObject().apply {
                    put("ImageBase64", imageBase64)
                    put("CardSide", cardSide)
                }.toString()

                // 生成签名
                val headers = TencentCloudSignature.generateSignature(
                    secretId = SECRET_ID,
                    secretKey = SECRET_KEY,
                    service = SERVICE,
                    host = HOST,
                    action = ACTION,
                    payload = payload,
                    timestamp = timestamp
                )

                Log.d(TAG, "Request payload: $payload")
                Log.d(TAG, "Request headers: $headers")

                // 发送请求
                val response = HttpUtil.post(ENDPOINT, headers, payload)
                Log.d(TAG, "Response: $response")

                response
            } catch (e: Exception) {
                Log.e(TAG, "OCR recognition failed", e)
                throw e
            }
        }
    }

    /**
     * 解析身份证识别结果
     * @param jsonResponse API返回的JSON字符串
     * @return 解析后的身份证信息Map
     */
    fun parseIDCardResult(jsonResponse: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        try {
            val json = JSONObject(jsonResponse)
            val response = json.getJSONObject("Response")

            if (response.has("Error")) {
                val error = response.getJSONObject("Error")
                result["error"] = error.getString("Message")
                return result
            }

            // 解析正面信息
            if (response.has("Name")) result["姓名"] = response.getString("Name")
            if (response.has("Sex")) result["性别"] = response.getString("Sex")
            if (response.has("Nation")) result["民族"] = response.getString("Nation")
            if (response.has("Birth")) result["出生日期"] = response.getString("Birth")
            if (response.has("Address")) result["住址"] = response.getString("Address")
            if (response.has("IdNum")) result["身份证号"] = response.getString("IdNum")

            // 解析反面信息
            if (response.has("Authority")) result["签发机关"] = response.getString("Authority")
            if (response.has("ValidDate")) result["有效期限"] = response.getString("ValidDate")

        } catch (e: Exception) {
            Log.e(TAG, "Parse result failed", e)
            result["error"] = "解析结果失败: ${e.message}"
        }
        return result
    }
}
