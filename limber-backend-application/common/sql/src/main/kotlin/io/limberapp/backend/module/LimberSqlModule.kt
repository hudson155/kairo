package io.limberapp.backend.module

import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.module.SqlModule
import com.piperframework.sql.registerJdbiType
import io.limberapp.backend.sql.type.JdbiFeaturePermissionsType
import io.limberapp.backend.sql.type.JdbiOrgPermissionsType
import org.jdbi.v3.core.Jdbi

open class LimberSqlModule(config: SqlDatabaseConfig) : SqlModule(config) {
  override fun Jdbi.configureJdbi() {
    registerJdbiType(JdbiFeaturePermissionsType)
    registerJdbiType(JdbiOrgPermissionsType)
  }
}
