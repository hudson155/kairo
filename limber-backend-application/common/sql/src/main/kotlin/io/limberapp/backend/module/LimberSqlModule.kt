package io.limberapp.backend.module

import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.module.SqlModule

open class LimberSqlModule(config: SqlDatabaseConfig) : SqlModule(config)
