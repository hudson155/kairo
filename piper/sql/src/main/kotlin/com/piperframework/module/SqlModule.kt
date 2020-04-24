package com.piperframework.module

import com.piperframework.config.database.SqlDatabaseConfig
import org.jetbrains.exposed.sql.Database

/**
 * SqlModule configures bindings for an SQL database.
 */
@Suppress("LateinitUsage")
open class SqlModule(config: SqlDatabaseConfig) : ModuleWithLifecycle() {
    protected val wrapper = SqlWrapper(config)

    override fun configure() {
        wrapper.connect()
        wrapper.runMigrations()
        bind(Database::class.java).toInstance(Database.connect(checkNotNull(wrapper.dataSource)))
    }

    override fun unconfigure() {
        wrapper.disconnect()
    }
}
