package io.limberapp.permissions.feature

import io.limberapp.logging.Logger
import io.limberapp.logging.LoggerFactory
import io.limberapp.permissions.Permission
import io.limberapp.util.darb.DarbEncoder

/**
 * Represents permissions for an unknown feature. Serialization and deserialization will work
 * correctly, but the [contains] method will always return false. See the type converter for
 * conditions that cause this class to be used.
 */
data class UnknownFeaturePermissions(
    override val prefix: Char,
    private val permissions: List<Boolean>,
) : FeaturePermissions() {
  companion object {
    fun fromDarb(prefix: Char, darb: String): UnknownFeaturePermissions =
        UnknownFeaturePermissions(prefix, DarbEncoder.decode(darb))
  }

  private val logger: Logger = LoggerFactory.getLogger(UnknownFeaturePermissions::class)

  override fun contains(permission: Permission): Boolean {
    logger.warn("Attempted to query unknown permission set for a permission. Defaulting to false.")
    return false
  }

  override fun asBooleanList(): List<Boolean> = permissions
}
