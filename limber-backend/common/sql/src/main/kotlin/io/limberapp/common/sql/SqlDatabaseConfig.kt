package io.limberapp.common.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.limberapp.common.config.database.SqlDatabaseConfig

fun SqlDatabaseConfig.createDataSource(): HikariDataSource {
  val hikariConfig = HikariConfig().apply {
    jdbcUrl = this@createDataSource.jdbcUrl.value
    username = this@createDataSource.username.value
    this@createDataSource.password?.let { password = it.value }
    this@createDataSource.connectionTimeout?.let { connectionTimeout = it }
    this@createDataSource.minimumIdle?.let { minimumIdle = it }
    this@createDataSource.maximumPoolSize?.let { maximumPoolSize = it }
    this@createDataSource.properties.forEach { addDataSourceProperty(it.key, it.value) }
  }
  return HikariDataSource(hikariConfig)
}
