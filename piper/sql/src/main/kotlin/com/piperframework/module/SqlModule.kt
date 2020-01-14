package com.piperframework.module

import com.google.inject.AbstractModule
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

/**
 * SqlModule configures bindings for an SQL database.
 */
open class SqlModule(sqlDatabaseConfig: SqlDatabaseConfig) : AbstractModule() {

    protected val dataSource = sqlDatabaseConfig.createDataSource().apply {
        runMigrations()
    }
    protected val database = Database.connect(dataSource)

    override fun configure() {
        bind(Database::class.java).toInstance(database)
    }

    private fun DataSource.runMigrations() {
        val flyway = Flyway.configure().dataSource(this).load()
        flyway.migrate()
    }
}
