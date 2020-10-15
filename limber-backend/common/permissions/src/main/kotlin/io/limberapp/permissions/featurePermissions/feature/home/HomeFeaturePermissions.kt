package io.limberapp.permissions.featurePermissions.feature.home

import io.limberapp.permissions.featurePermissions.FeaturePermissions
import io.limberapp.util.darb.BitStringEncoder

/**
 * All home feature permissions, in the correct order, with a quick sanity check on the digits.
 */
private val ALL_HOME_FEATURE_PERMISSIONS = with(HomeFeaturePermission.values()) {
  sortedBy { it.bit }
      .apply { check(this@apply.map { it.bit } == this@with.indices.map { it }) }
}

internal const val HOME_FEATURE_PREFIX = 'H'

class HomeFeaturePermissions(override val permissions: Set<HomeFeaturePermission>) : FeaturePermissions() {
  override val prefix = HOME_FEATURE_PREFIX

  override fun allPermissions() = ALL_HOME_FEATURE_PERMISSIONS

  companion object {
    fun none() = HomeFeaturePermissions(emptySet())

    fun fromBitString(bitString: String) = fromBooleanList(BitStringEncoder.decode(bitString))

    internal fun fromBooleanList(booleanList: List<Boolean>) = HomeFeaturePermissions(
        permissions = ALL_HOME_FEATURE_PERMISSIONS
            .filterIndexed { i, _ -> booleanList.getOrNull(i) == true }
            .toSet()
    )

    fun Collection<HomeFeaturePermissions>.union() = HomeFeaturePermissions(
        permissions = fold(emptySet()) { acc, permissions -> acc.union(permissions.permissions) }
    )
  }
}
