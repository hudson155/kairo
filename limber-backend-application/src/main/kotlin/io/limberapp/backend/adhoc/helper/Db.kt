package io.limberapp.backend.adhoc.helper

import com.piperframework.config.ConfigString
import com.piperframework.config.database.SqlDatabaseConfig

internal fun dbConfig(dbName: String?) = SqlDatabaseConfig(
    jdbcUrl = ConfigString(
        type = ConfigString.Type.PLAINTEXT,
        value = "jdbc:postgresql://localhost/${dbName ?: "limber"}"
    ),
    username = "postgres",
    password = null
)
