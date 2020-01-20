package io.limberapp.backend.config

import com.google.cloud.kms.v1.CryptoKeyName
import com.google.cloud.kms.v1.KeyManagementServiceClient
import com.google.protobuf.ByteString
import com.piperframework.config.Config
import com.piperframework.config.ConfigString
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.config.serving.ServingConfig
import java.nio.charset.Charset
import java.util.Base64

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
internal data class LimberAppMonolithConfig(
    val kmsConfig: KmsConfig?,
    val sqlDatabase: SqlDatabaseConfig,
    override val authentication: AuthenticationConfig,
    override val serving: ServingConfig
) : Config {

    fun decrypt() = copy(sqlDatabase = sqlDatabase.decrypt())

    private fun SqlDatabaseConfig.decrypt() = copy(password = password?.decrypt())

    private fun ConfigString.decrypt(): ConfigString {
        return when (type) {
            ConfigString.Type.PLAINTEXT -> this
            ConfigString.Type.ENVIRONMENT_VARIABLE -> copy(value = checkNotNull(System.getenv(this.value)))
            ConfigString.Type.ENCRYPTED -> copy(value = decrypt(value))
        }
    }

    private fun decrypt(value: String): String {
        checkNotNull(kmsConfig)
        val decoded = Base64.getDecoder().decode(value)
        // Create the KeyManagementServiceClient using try-with-resources to manage client cleanup.
        return KeyManagementServiceClient.create().use { client ->
            return@use client.decrypt(
                CryptoKeyName.of(kmsConfig.project, kmsConfig.location, kmsConfig.keyRing, kmsConfig.cryptoKey),
                ByteString.copyFrom(decoded)
            ).plaintext.toString(Charset.defaultCharset())
        }
    }
}
