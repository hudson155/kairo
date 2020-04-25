package com.piperframework.module

import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import com.zaxxer.hikari.HikariDataSource
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
