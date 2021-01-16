package io.limberapp.common.permissions

import io.limberapp.common.util.darb.BitStringEncoder
import io.limberapp.common.util.darb.DarbEncoder

/**
 * Generic class to represent a set of permissions. Permissions are serialized using DARB.
 */
abstract class Permissions<P : Permission> internal constructor(
    private val permissions: Set<P>,
) : Set<P> by permissions {
  /**
   * Must return all permissions in order. The order used here is the order used for encoding and
   * serialization, so it must be consistent. If the permission class [P] has n permissions, a list
   * of length n is expected, in ascending order of bits.
   *
   * The implementation should invariably look like:
   * override fun asBooleanList() = values.map { it in this }
   */
  protected abstract fun asBooleanList(): List<Boolean>

  open fun asDarb(): String = DarbEncoder.encode(asBooleanList())

  open fun asBitString(): String = BitStringEncoder.encode(asBooleanList())

  abstract class Companion<P : Permission, S : Permissions<P>>(
      protected val values: Array<P>,
  ) {
    init {
      // Ensure [values] is the permissions in monotonically increasing bit index order.
      check(values.map { permission -> permission.index } == values.indices.toList())
    }

    fun none(): S =
        fromBooleanList(emptyList())

    open fun fromDarb(darb: String): S =
        fromBooleanList(DarbEncoder.decode(darb))

    open fun fromBitString(bitString: String): S =
        fromBooleanList(BitStringEncoder.decode(bitString))

    protected abstract fun fromBooleanList(booleanList: List<Boolean>): S

    protected fun from(booleanList: List<Boolean>): Set<P> = values
        .filterIndexed { i, _ -> booleanList.getOrNull(i) == true }
        .toSet()
  }
}
