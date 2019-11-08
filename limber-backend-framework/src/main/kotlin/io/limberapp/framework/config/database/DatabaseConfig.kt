package io.limberapp.framework.config.database

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

/**
 * This class encapsulates the configuration for the connection to MongoDB. The class name does not
 * specify that the database is MongoDB, so if/when multiple databases are introduced, this should
 * be renamed.
 */
data class DatabaseConfig(
    val protocol: Protocol,
    val host: String,
    val database: String,
    val user: String?,
    val password: EncryptedValue?
) {

    enum class Protocol(private val text: String) {
        MONGODB("mongodb"),
        @Suppress("unused")
        MONGODB_SRV("mongodb+srv"),
        ;

        override fun toString() = text
    }

    companion object {
        fun local(database: String) = DatabaseConfig(
            protocol = Protocol.MONGODB,
            host = "localhost",
            database = database,
            user = null,
            password = null
        )
    }
}
