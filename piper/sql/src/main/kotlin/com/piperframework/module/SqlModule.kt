package com.piperframework.module

import com.google.inject.AbstractModule
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.createDataSource
import javax.sql.DataSource

/**
 * MongoModule configures bindings for an SQL database.
 */
open class SqlModule(private val sqlDatabaseConfig: SqlDatabaseConfig) : AbstractModule() {

    override fun configure() {
        bind(DataSource::class.java).toInstance(sqlDatabaseConfig.createDataSource())
    }
}
