package io.limberapp.backend.module

import io.limberapp.backend.sql.type.JdbiFeaturePermissionsType
import io.limberapp.backend.sql.type.JdbiOrgPermissionsType
import io.limberapp.common.config.database.SqlDatabaseConfig
import io.limberapp.common.module.SqlModule
import io.limberapp.common.sql.registerJdbiType
import org.jdbi.v3.core.Jdbi

open class LimberSqlModule(config: SqlDatabaseConfig, runMigrations: Boolean) : SqlModule(config, runMigrations) {
  override fun Jdbi.configureJdbi() {
    registerJdbiType(JdbiFeaturePermissionsType)
    registerJdbiType(JdbiOrgPermissionsType)
  }
}
