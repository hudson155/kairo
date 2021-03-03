package io.limberapp.testing.integration

import io.limberapp.permissions.feature.TestFeaturePermissions
import io.limberapp.typeConversion.TypeConverter
import io.limberapp.typeConversion.typeConverter.FeaturePermissionsTypeConverter

internal object TypeConversionModule : AbstractTypeConversionModule() {
  override val typeConverters: Set<TypeConverter<*>> = setOf(
      FeaturePermissionsTypeConverter(mapOf('T' to TestFeaturePermissions)),
  )
}
