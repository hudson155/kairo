package com.piperframework.config.database

import com.piperframework.config.EncryptedValue

/**
 * This class encapsulates the configuration for the connection to MongoDB.
 */
data class MongoDatabaseConfig(
    val protocol: Protocol,
    val host: String,
    val database: String,
    val user: String?,
    val password: EncryptedValue?
) {

    enum class Protocol(private val text: String) {

        MONGODB("mongodb"),
        @Suppress("unused")
        MONGODB_SRV("mongodb+srv");

        override fun toString() = text
    }

    companion object {
        fun local(database: String) = MongoDatabaseConfig(
            protocol = Protocol.MONGODB,
            host = "localhost",
            database = database,
            user = null,
            password = null
        )
    }
}
