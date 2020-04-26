package io.limberapp.backend.adhoc.helper

import com.piperframework.config.ConfigString
import com.piperframework.config.database.SqlDatabaseConfig

internal fun dbConfig(jdbcUrl: String, username: String?, password: String?) = SqlDatabaseConfig(
    jdbcUrl = ConfigString(type = ConfigString.Type.PLAINTEXT, value = "jdbc:postgresql://$jdbcUrl"),
    username = username ?: "postgres",
    password = password?.let { ConfigString(type = ConfigString.Type.PLAINTEXT, value = it) }
)
