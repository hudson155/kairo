package io.limberapp.backend.sql.type

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.sql.JdbiType
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.mapper.ColumnMapper
import java.sql.Types

internal object JdbiFeaturePermissionsType : JdbiType<FeaturePermissions>() {
  override val kClass = FeaturePermissions::class

  override val columnMapper: ColumnMapper<FeaturePermissions?> = ColumnMapper { r, columnNumber, _ ->
    r.getString(columnNumber)?.let { FeaturePermissions.fromBitString(it) }
  }

  override val argumentFactory: AbstractArgumentFactory<FeaturePermissions> =
    object : AbstractArgumentFactory<FeaturePermissions>(Types.VARCHAR) {
      override fun build(value: FeaturePermissions, config: ConfigRegistry): Argument =
        Argument { position, statement, _ -> statement.setString(position, value.asBitString()) }
    }
}
