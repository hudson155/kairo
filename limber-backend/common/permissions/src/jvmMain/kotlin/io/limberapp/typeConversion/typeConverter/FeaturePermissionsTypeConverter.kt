package io.limberapp.typeConversion.typeConverter

import io.limberapp.permissions.Permissions
import io.limberapp.permissions.feature.FeaturePermissions
import io.limberapp.permissions.feature.UnknownFeaturePermissions
import io.limberapp.typeConversion.TypeConverter
import kotlin.reflect.KClass

class FeaturePermissionsTypeConverter(
    private val types: Map<Char, Permissions.Companion<*, out FeaturePermissions>>,
) : TypeConverter<FeaturePermissions> {
  override val kClass: KClass<FeaturePermissions> = FeaturePermissions::class

  override fun isValid(value: String): Boolean? = null

  override fun parseString(value: String): FeaturePermissions {
    val (prefixString, darb) = value.split('.', limit = 2)
    val prefixChar = prefixString.singleOrNull() ?: error("$prefixString is not a valid prefix.")
    val type = types[prefixChar]
    // It's possible that the prefix is not in scope. This doesn't necessarily mean it's wrong, so
    // we shouldn't throw an exception, but we can't do anything useful with it.
    type ?: return UnknownFeaturePermissions.fromDarb(prefixChar, darb)
    return type.fromDarb(darb)
  }

  override fun writeString(value: FeaturePermissions): String = value.asDarb()
}
