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
        .apply {
            check(this@apply.map { it.bit } == this@with.indices.map { it })
        }
}

@Serializable(with = OrgPermissionsSerializer::class)
data class OrgPermissions(private val booleanList: List<Boolean>) {
    private val permissions = ALL_ORG_PERMISSIONS.filterIndexed { i, _ -> booleanList.getOrNull(i) == true }.toSet()

    fun asDarb() = DarbEncoder.encode(booleanList)

    fun asBitString() = BitStringEncoder.encode(booleanList)

    companion object {
        fun none() = OrgPermissions(ALL_ORG_PERMISSIONS.map { false })

        fun fromDarb(darb: String) = OrgPermissions(DarbEncoder.decode(darb))

        fun fromBitString(bitString: String) = OrgPermissions(BitStringEncoder.decode(bitString))
    }
}

object OrgPermissionsSerializer : KSerializer<OrgPermissions> {
    override val descriptor = PrimitiveDescriptor("OrgPermissions", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: OrgPermissions) = encoder.encodeString(value.asDarb())
    override fun deserialize(decoder: Decoder) = OrgPermissions.fromDarb(decoder.decodeString())
}
