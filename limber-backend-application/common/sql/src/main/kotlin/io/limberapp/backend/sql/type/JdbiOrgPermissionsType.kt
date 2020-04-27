package io.limberapp.backend.sql.type

import com.piperframework.sql.JdbiType
import io.limberapp.backend.authorization.permissions.OrgPermissions
import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.mapper.ColumnMapper
import java.sql.Types

internal object JdbiOrgPermissionsType : JdbiType<OrgPermissions>() {
    override val kClass = OrgPermissions::class

    override val columnMapper: ColumnMapper<OrgPermissions?> = ColumnMapper { r, columnNumber, _ ->
        r.getString(columnNumber)?.let { OrgPermissions.fromBitString(it) }
    }

    override val argumentFactory: AbstractArgumentFactory<OrgPermissions> =
        object : AbstractArgumentFactory<OrgPermissions>(Types.VARCHAR) {
            override fun build(value: OrgPermissions, config: ConfigRegistry): Argument =
                Argument { position, statement, _ -> statement.setString(position, value.asBitString()) }
        }
}
