package io.limberapp.common.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.limberapp.backend.config.SqlDatabaseConfig

@Suppress("ExplicitThis")
fun SqlDatabaseConfig.createDataSource(): HikariDataSource {
  val hikariConfig = HikariConfig().apply {
    this@createDataSource.jdbcUrl.let {
      jdbcUrl = it
    }
    this@createDataSource.defaultSchema.let {
      schema = it
    }
    this@createDataSource.username.let {
      username = it
    }
    this@createDataSource.password?.let {
      password = it
    }
    this@createDataSource.connectionTimeout?.let {
      connectionTimeout = it
    }
    this@createDataSource.minimumIdle?.let {
      minimumIdle = it
    }
    this@createDataSource.maximumPoolSize?.let {
      maximumPoolSize = it
    }
    this@createDataSource.properties.forEach {
      addDataSourceProperty(it.key, it.value)
    }
  }
  return HikariDataSource(hikariConfig)
}
