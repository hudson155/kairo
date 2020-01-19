package com.piperframework.sql

import com.piperframework.config.database.SqlDatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun SqlDatabaseConfig.createDataSource(): HikariDataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = this@createDataSource.jdbcUrl
        this@createDataSource.username?.let { username = it }
        this@createDataSource.password?.let { password = it }
        this@createDataSource.properties.forEach { addDataSourceProperty(it.key, it.value) }
    }
    return HikariDataSource(hikariConfig)
}
