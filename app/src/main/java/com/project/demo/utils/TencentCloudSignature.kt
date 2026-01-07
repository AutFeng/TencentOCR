package com.project.demo.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 腾讯云API V3签名工具类
 * 实现腾讯云API V3版本的签名算法
 */
object TencentCloudSignature {

    private const val ALGORITHM = "TC3-HMAC-SHA256"

    /**
     * 生成腾讯云API V3签名
     * @param secretId 密钥ID
     * @param secretKey 密钥Key
     * @param service 服务名称，如"ocr"
     * @param host 请求域名
     * @param action API操作名称
     * @param payload 请求体JSON字符串
     * @param timestamp 时间戳（秒）
     * @return 包含Authorization和其他必要头部的Map
     */
    fun generateSignature(
        secretId: String,
        secretKey: String,
        service: String,
        host: String,
        action: String,
        payload: String,
        timestamp: Long
    ): Map<String, String> {

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(timestamp * 1000))

        // 1. 拼接规范请求串
        val httpRequestMethod = "POST"
        val canonicalUri = "/"
        val canonicalQueryString = ""
        val canonicalHeaders = "content-type:application/json\nhost:$host\n"
        val signedHeaders = "content-type;host"
        val hashedRequestPayload = sha256Hex(payload)

        val canonicalRequest = "$httpRequestMethod\n$canonicalUri\n$canonicalQueryString\n" +
                "$canonicalHeaders\n$signedHeaders\n$hashedRequestPayload"

        // 2. 拼接待签名字符串
        val credentialScope = "$date/$service/tc3_request"
        val hashedCanonicalRequest = sha256Hex(canonicalRequest)
        val stringToSign = "$ALGORITHM\n$timestamp\n$credentialScope\n$hashedCanonicalRequest"

        // 3. 计算签名
        val secretDate = hmacSha256(date.toByteArray(StandardCharsets.UTF_8),
            "TC3$secretKey".toByteArray(StandardCharsets.UTF_8))
        val secretService = hmacSha256(service.toByteArray(StandardCharsets.UTF_8), secretDate)
        val secretSigning = hmacSha256("tc3_request".toByteArray(StandardCharsets.UTF_8), secretService)
        val signature = hmacSha256Hex(stringToSign.toByteArray(StandardCharsets.UTF_8), secretSigning)

        // 4. 拼接Authorization
        val authorization = "$ALGORITHM Credential=$secretId/$credentialScope, " +
                "SignedHeaders=$signedHeaders, Signature=$signature"

        return mapOf(
            "Authorization" to authorization,
            "Content-Type" to "application/json",
            "Host" to host,
            "X-TC-Action" to action,
            "X-TC-Timestamp" to timestamp.toString(),
            "X-TC-Version" to "2018-11-19"
        )
    }

    /**
     * SHA256哈希并转换为十六进制字符串
     */
    private fun sha256Hex(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hash)
    }

    /**
     * HMAC-SHA256加密
     */
    private fun hmacSha256(data: ByteArray, key: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data)
    }

    /**
     * HMAC-SHA256加密并转换为十六进制字符串
     */
    private fun hmacSha256Hex(data: ByteArray, key: ByteArray): String {
        return bytesToHex(hmacSha256(data, key))
    }

    /**
     * 字节数组转十六进制字符串
     */
    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
