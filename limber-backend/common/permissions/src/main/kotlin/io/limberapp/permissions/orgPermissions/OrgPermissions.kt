package io.limberapp.permissions.orgPermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.limberapp.common.util.darb.BitStringEncoder
import io.limberapp.common.util.darb.DarbEncoder
import io.limberapp.permissions.Permissions

/**
 * All permissions, in the correct order, with a quick sanity check on the digits. If this sanity check fails, double
 * check the bit values in the [OrgPermission] enum. They should be monotonically increasing from 0. This check only
 * runs when the app starts.
 */
private val ALL_ORG_PERMISSIONS = with(OrgPermission.values()) {
  sortedBy { it.bit }
      .apply { check(this@apply.map { it.bit } == this@with.indices.map { it }) }
}

/**
 * Permissions that apply organization-wide.
 */
@JsonDeserialize(using = OrgPermissions.Deserializer::class)
data class OrgPermissions(override val permissions: Set<OrgPermission>) : Permissions<OrgPermission>() {
  override val prefix: Char? = null // OrgPermissions is not prefixed.

  override fun allPermissions() = ALL_ORG_PERMISSIONS

  companion object {
    fun none() = OrgPermissions(emptySet())

    fun fromDarb(darb: String) = fromBooleanList(DarbEncoder.decode(darb))

    fun fromBitString(bitString: String) = fromBooleanList(BitStringEncoder.decode(bitString))

    private fun fromBooleanList(booleanList: List<Boolean>) = OrgPermissions(
        permissions = ALL_ORG_PERMISSIONS
            .filterIndexed { i, _ -> booleanList.getOrNull(i) == true }
            .toSet()
    )

    fun Collection<OrgPermissions>.union() = OrgPermissions(
        permissions = fold(emptySet()) { acc, permissions -> acc.union(permissions.permissions) }
    )
  }

  class Deserializer : StdDeserializer<OrgPermissions>(OrgPermissions::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromDarb(p.text)
  }
}
