package com.piperframework.sql

import com.piperframework.config.database.SqlDatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun SqlDatabaseConfig.createDataSource(): HikariDataSource {
  val hikariConfig = HikariConfig().apply {
    jdbcUrl = this@createDataSource.jdbcUrl.value
    username = this@createDataSource.username
    this@createDataSource.password?.let { password = it.value }
    this@createDataSource.connectionTimeout?.let { connectionTimeout = it }
    this@createDataSource.minimumIdle?.let { minimumIdle = it }
    this@createDataSource.maximumPoolSize?.let { maximumPoolSize = it }
    this@createDataSource.properties.forEach { addDataSourceProperty(it.key, it.value) }
  }
  return HikariDataSource(hikariConfig)
}
