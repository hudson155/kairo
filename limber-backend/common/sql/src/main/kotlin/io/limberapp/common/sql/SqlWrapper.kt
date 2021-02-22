package io.limberapp.common.sql

import com.zaxxer.hikari.HikariDataSource
import io.limberapp.backend.config.SqlDatabaseConfig
import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection

class SqlWrapper(private val config: SqlDatabaseConfig) {
  private val logger: Logger = LoggerFactory.getLogger(SqlWrapper::class.java)

  var dataSource: HikariDataSource? = null
    private set

  fun createJdbi(): Jdbi = Jdbi.create(dataSource)

  val connection: Connection get() = checkNotNull(dataSource).connection

  fun connect() {
    logger.info("Creating SQL data source...")
    check(dataSource == null) { "Already connected." }
    dataSource = config.createDataSource()
  }

  fun runMigrations() {
    logger.info("Running SQL migrations...")
    val flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()
  }

  fun disconnect() {
    logger.info("Closing SQL data source...")
    checkNotNull(dataSource) { "Not connected." }.close()
    dataSource = null
  }
}
