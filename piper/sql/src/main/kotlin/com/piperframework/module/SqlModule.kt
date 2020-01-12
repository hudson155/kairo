package com.piperframework.module

import com.google.inject.AbstractModule
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

/**
 * MongoModule configures bindings for an SQL database.
 */
open class SqlModule(private val sqlDatabaseConfig: SqlDatabaseConfig) : AbstractModule() {

    override fun configure() {
        val dataSource = sqlDatabaseConfig.createDataSource()
        dataSource.runMigrations()
        val database = Database.connect(dataSource)
        bind(Database::class.java).toInstance(database)
    }

    private fun DataSource.runMigrations() {
        val flyway = Flyway.configure().dataSource(this).load()
        flyway.migrate()
    }
}
