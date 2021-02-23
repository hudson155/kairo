package io.limberapp.common.typeConversion

import io.limberapp.common.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import org.jdbi.v3.core.Jdbi
import java.util.UUID

internal object TypeConverterInstaller {
  private enum class SqlHandling { AS_STRING }

  fun install(jdbi: Jdbi, typeConverters: Set<TypeConverter<*>>) {
    typeConverters.forEach { typeConverter ->
      when (sqlHandling(typeConverter)) {
        null -> Unit
        SqlHandling.AS_STRING -> run<Unit> {
          jdbi.registerColumnMapper(typeConverter.kClass.java, AsStringColumnMapper(typeConverter))
          jdbi.registerArgument(AsStringArgumentFactory(typeConverter))
        }
      }
    }
  }

  private fun sqlHandling(typeConverter: TypeConverter<*>): SqlHandling? =
      when (typeConverter.kClass) {
        FeaturePermissions::class -> SqlHandling.AS_STRING
        LimberPermissions::class -> SqlHandling.AS_STRING
        OrgPermissions::class -> SqlHandling.AS_STRING
        Regex::class -> SqlHandling.AS_STRING
        UUID::class -> null // UUIDs are mapped automatically by the Postgres driver.
        else -> null
      }
}
