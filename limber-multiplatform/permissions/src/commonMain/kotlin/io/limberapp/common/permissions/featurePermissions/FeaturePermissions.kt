package io.limberapp.common.permissions.featurePermissions

import io.limberapp.common.permissions.Permissions

/**
 * [FeaturePermissions] are unique per feature, and should therefore be defined within the library
 * for that feature. [FeaturePermissions] are encoded and decoded differently than other
 * permissions, since they have a prefix representing which feature type they are for.
 */
abstract class FeaturePermissions<P : FeaturePermission>(
    permissions: Set<P>,
) : Permissions<P>(permissions) {
  abstract val prefix: Char

  final override fun asDarb() = "$prefix." + super.asDarb()

  final override fun asBitString() = prefix + super.asBitString()

  abstract class Companion<P : FeaturePermission, S : FeaturePermissions<P>>(
      values: Array<P>,
  ) : Permissions.Companion<P, S>(values) {
    /**
     * Each type of permissions needs a unique prefix so that the permissions can be understood when
     * deserializing.
     */
    abstract val prefix: Char

    final override fun fromDarb(darb: String): S =
        super.fromDarb(stripPrefix(darb, "$prefix."))

    final override fun fromBitString(bitString: String): S =
        super.fromBitString(stripPrefix(bitString, "$prefix"))

    private fun stripPrefix(string: String, prefix: String): String {
      if (!string.startsWith(prefix)) error("Expected prefix $prefix in $string.")
      return string.substring(prefix.length)
    }
  }
}
