package com.piperframework.sql

import com.piperframework.config.database.SqlDatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun SqlDatabaseConfig.createDataSource(): DataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = this@createDataSource.jdbcUrl
        username = this@createDataSource.username
    }
    return HikariDataSource(hikariConfig)
}
