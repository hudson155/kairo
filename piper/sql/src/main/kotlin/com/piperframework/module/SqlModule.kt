package com.piperframework.module

import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.sql.registerJdbiType
import com.piperframework.sql.type.JdbiRegexType
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

/**
 * SqlModule configures bindings for an SQL database.
 */
@Suppress("LateinitUsage")
open class SqlModule(config: SqlDatabaseConfig, private val runMigrations: Boolean) : ModuleWithLifecycle() {
  protected val wrapper = SqlWrapper(config)

  override fun configure() {
    wrapper.connect()
    if (runMigrations) wrapper.runMigrations()
    bind(Jdbi::class.java).toInstance(createJdbi())
  }

  private fun createJdbi() = Jdbi.create(checkNotNull(wrapper.dataSource))
    .installPlugin(KotlinPlugin())
    .installPlugin(KotlinSqlObjectPlugin())
    .installPlugin(PostgresPlugin())
    .registerJdbiType(JdbiRegexType)
    .apply { configureJdbi() }

  protected open fun Jdbi.configureJdbi() {}

  override fun unconfigure() {
    wrapper.disconnect()
  }
}
