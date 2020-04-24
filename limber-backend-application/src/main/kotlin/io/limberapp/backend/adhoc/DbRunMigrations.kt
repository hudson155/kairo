package io.limberapp.backend.adhoc

import com.piperframework.config.ConfigString
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.module.SqlWrapper

fun main(args: Array<String>) {
    val config = SqlDatabaseConfig(
        jdbcUrl = ConfigString(
            type = ConfigString.Type.PLAINTEXT,
            value = "jdbc:postgresql://localhost/${args.singleOrNull() ?: "limber"}"
        ),
        username = "postgres",
        password = null
    )
    with(SqlWrapper(config)) {
        connect()
        runMigrations()
        disconnect()
    }
}
