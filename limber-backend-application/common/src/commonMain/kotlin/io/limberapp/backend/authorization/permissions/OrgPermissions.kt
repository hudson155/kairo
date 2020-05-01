package io.limberapp.backend.authorization.permissions

import com.piperframework.util.darb.BitStringEncoder
import com.piperframework.util.darb.DarbEncoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable

/**
 * All permissions, in the correct order, with a quick sanity check on the digits. If this sanity check fails, double
 * check the bit values in the [OrgPermission] enum. They should be monotonically increasing from 0. This check only
 * runs when the app starts.
 */
private val ALL_ORG_PERMISSIONS = with(OrgPermission.values()) {
    sortedBy { it.bit }
        .apply { check(this@apply.map { it.bit } == this@with.indices.map { it }) }
}

@Serializable(with = OrgPermissionsSerializer::class)
data class OrgPermissions(private val permissions: Set<OrgPermission>) {
    private val booleanList = ALL_ORG_PERMISSIONS.map { it in permissions }

    fun hasPermission(permission: OrgPermission) = permission in permissions

    fun asDarb() = DarbEncoder.encode(booleanList)

    fun asBitString() = BitStringEncoder.encode(booleanList)

    override fun toString() = asDarb()

    companion object {
        fun none() = OrgPermissions(emptySet())

        fun fromDarb(darb: String) = fromBooleanList(DarbEncoder.decode(darb))

        fun fromBitString(bitString: String) = fromBooleanList(BitStringEncoder.decode(bitString))

        private fun fromBooleanList(booleanList: List<Boolean>) =
            OrgPermissions(ALL_ORG_PERMISSIONS.filterIndexed { i, _ -> booleanList.getOrNull(i) == true }.toSet())

        fun Collection<OrgPermissions>.union() = OrgPermissions(
            permissions = fold(emptySet()) { acc, permissions -> acc.union(permissions.permissions) }
        )
    }
}

object OrgPermissionsSerializer : KSerializer<OrgPermissions> {
    override val descriptor = PrimitiveDescriptor("OrgPermissions", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: OrgPermissions) = encoder.encodeString(value.asDarb())
    override fun deserialize(decoder: Decoder) = OrgPermissions.fromDarb(decoder.decodeString())
}
