package io.limberapp.permissions.feature

import io.limberapp.permissions.Permissions

/**
 * [FeaturePermissions] are unique per feature, and should therefore be defined within the library
 * for that feature. [FeaturePermissions] are encoded and decoded differently than other
 * permissions, since they have a prefix representing which feature type they are for.
 */
abstract class FeaturePermissions : Permissions() {
  abstract val prefix: Char

  override fun asDarb(): String = "$prefix." + super.asDarb()

  override fun asBitString(): String = prefix + super.asBitString()
}
