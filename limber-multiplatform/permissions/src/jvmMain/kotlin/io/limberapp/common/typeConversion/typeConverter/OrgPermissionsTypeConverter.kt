package io.limberapp.common.typeConversion.typeConverter

import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.typeConversion.TypeConverter
import kotlin.reflect.KClass

object OrgPermissionsTypeConverter : TypeConverter<OrgPermissions> {
  override val kClass: KClass<OrgPermissions> = OrgPermissions::class

  override fun isValid(value: String): Boolean? = null

  override fun parseString(value: String): OrgPermissions = OrgPermissions.fromDarb(value)

  override fun writeString(value: OrgPermissions): String = value.asDarb()
}
