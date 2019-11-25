package com.piperframework.config

import com.google.cloud.kms.v1.CryptoKeyName
import com.google.cloud.kms.v1.KeyManagementServiceClient
import com.google.protobuf.ByteString
import java.nio.charset.Charset
import java.util.Base64

data class EncryptedValue(
    private val encrypted: Boolean,
    private val value: String
) {

    val decryptedValue: String = run {
        if (!encrypted) return@run value
        val decoded = Base64.getDecoder().decode(value)
        // Create the KeyManagementServiceClient using try-with-resources to manage client cleanup.
        return@run KeyManagementServiceClient.create().use { client ->
            return@use client.decrypt(
                CryptoKeyName.of("limberapp", "global", "limberapp", "secrets"),
                ByteString.copyFrom(decoded)
            ).plaintext.toString(Charset.defaultCharset())
        }
    }
}
