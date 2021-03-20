package io.limberapp.typeConversion.typeConverter

import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.typeConversion.TypeConverter
import kotlin.reflect.KClass

object LimberPermissionsTypeConverter : TypeConverter<LimberPermissions> {
  override val kClass: KClass<LimberPermissions> = LimberPermissions::class

  override fun isValid(value: String): Boolean? = null

  override fun parseString(value: String): LimberPermissions = LimberPermissions.fromDarb(value)

  override fun writeString(value: LimberPermissions): String = value.asDarb()
}
