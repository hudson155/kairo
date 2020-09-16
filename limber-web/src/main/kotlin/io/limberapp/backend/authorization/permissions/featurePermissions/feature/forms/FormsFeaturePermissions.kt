package io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.util.darb.BitStringEncoder

/**
 * All forms feature permissions, in the correct order, with a quick sanity check on the digits.
 */
private val ALL_FORMS_FEATURE_PERMISSIONS = with(FormsFeaturePermission.values()) {
  sortedBy { it.bit }
    .apply { check(this@apply.map { it.bit } == this@with.indices.map { it }) }
}

internal const val FORMS_FEATURE_PREFIX = 'F'

data class FormsFeaturePermissions(override val permissions: Set<FormsFeaturePermission>) : FeaturePermissions() {
  override val prefix = FORMS_FEATURE_PREFIX

  override fun allPermissions() = ALL_FORMS_FEATURE_PERMISSIONS

  companion object {
    fun none() = FormsFeaturePermissions(emptySet())

    fun fromBitString(bitString: String) = fromBooleanList(BitStringEncoder.decode(bitString))

    internal fun fromBooleanList(booleanList: List<Boolean>) = FormsFeaturePermissions(
      permissions = ALL_FORMS_FEATURE_PERMISSIONS
        .filterIndexed { i, _ -> booleanList.getOrNull(i) == true }
        .toSet()
    )
  }
}
