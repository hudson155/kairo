package io.limberapp.backend.module

import io.limberapp.backend.sql.type.JdbiFeaturePermissionsType
import io.limberapp.backend.sql.type.JdbiOrgPermissionsType
import io.limberapp.common.config.database.SqlDatabaseConfig
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.module.SqlWrapper
import io.limberapp.common.sql.registerJdbiType
import io.limberapp.common.sql.type.JdbiRegexType
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

/**
 * SqlModule configures bindings for an SQL database.
 */
open class SqlModule(config: SqlDatabaseConfig, private val runMigrations: Boolean) : GuiceModule() {
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
      .registerJdbiType(JdbiFeaturePermissionsType)
      .registerJdbiType(JdbiOrgPermissionsType)

  override fun unconfigure() {
    wrapper.disconnect()
  }
}
