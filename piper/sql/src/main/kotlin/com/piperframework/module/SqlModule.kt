package com.piperframework.module

import com.google.inject.AbstractModule
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

/**
 * MongoModule configures bindings for an SQL database.
 */
open class SqlModule(private val sqlDatabaseConfig: SqlDatabaseConfig) : AbstractModule() {

    override fun configure() {
        bindDataSource()
    }

    private fun bindDataSource() {
        val dataSource = sqlDatabaseConfig.createDataSource()
        dataSource.runMigrations()
        bind(DataSource::class.java).toInstance(dataSource)
    }

    private fun DataSource.runMigrations() {
        val flyway = Flyway.configure().dataSource(this).load()
        flyway.migrate()
    }
}
