package com.piperframework.module

import com.piperframework.config.database.SqlDatabaseConfig
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
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
        bind(Jdbi::class.java).toInstance(createJdbi())
    }

    private fun createJdbi() = Jdbi.create(checkNotNull(wrapper.dataSource))
        .installPlugin(KotlinPlugin())
        .installPlugin(KotlinSqlObjectPlugin())
        .installPlugin(PostgresPlugin())

    override fun unconfigure() {
        wrapper.disconnect()
    }
}
