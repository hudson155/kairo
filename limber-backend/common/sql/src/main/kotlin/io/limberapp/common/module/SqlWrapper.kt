package io.limberapp.common.module

import com.zaxxer.hikari.HikariDataSource
import io.limberapp.common.config.database.SqlDatabaseConfig
import io.limberapp.common.sql.createDataSource
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory

class SqlWrapper(private val config: SqlDatabaseConfig) {
  private val logger = LoggerFactory.getLogger(SqlWrapper::class.java)

  var dataSource: HikariDataSource? = null
    private set

  fun connect() {
    check(dataSource == null)
    logger.info("Creating SQL data source...")
    dataSource = config.createDataSource()
  }

  fun runMigrations() {
    logger.info("Running SQL migrations...")
    val flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()
  }

  fun disconnect() {
    logger.info("Closing SQL data source...")
    checkNotNull(dataSource).close()
  }
}
