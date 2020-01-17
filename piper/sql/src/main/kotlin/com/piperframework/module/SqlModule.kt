package com.piperframework.module

import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import javax.sql.DataSource

/**
 * SqlModule configures bindings for an SQL database.
 */
@Suppress("LateinitUsage")
open class SqlModule(private val sqlDatabaseConfig: SqlDatabaseConfig) : ModuleWithLifecycle() {

    private val logger = LoggerFactory.getLogger(SqlModule::class.java)

    protected lateinit var dataSource: HikariDataSource

    override fun configure() {

        logger.info("Creating SQL data source...")
        dataSource = sqlDatabaseConfig.createDataSource()
        logger.info("Running SQL migrations...")
        dataSource.runMigrations()

        bind(Database::class.java).toInstance(Database.connect(dataSource))
    }

    override fun unconfigure() {
        logger.info("Closing SQL data source...")
        dataSource.close()
    }

    fun close() = dataSource.close()

    private fun DataSource.runMigrations() {
        val flyway = Flyway.configure().dataSource(this).load()
        flyway.migrate()
    }
}
