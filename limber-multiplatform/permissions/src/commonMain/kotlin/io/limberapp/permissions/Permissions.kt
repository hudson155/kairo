package io.limberapp.permissions

import io.limberapp.util.darb.BitStringEncoder
import io.limberapp.util.darb.DarbEncoder

/**
 * Abstract class to represent a set of permissions. Permissions are serialized using DARB.
 */
abstract class Permissions internal constructor() {
  abstract class Companion<P : Permission, S : Permissions>(
      protected val values: Array<P>,
  ) {
    init {
      // Ensure [values] is the permissions in monotonically increasing bit index order.
      check(values.map { permission -> permission.index } == values.indices.toList())
    }

    fun none(): S = fromBooleanList(emptyList())

    fun all(): S = fromBooleanList(List(values.size) { true })

    fun fromDarb(darb: String): S =
        fromBooleanList(DarbEncoder.decode(darb))

    fun fromBitString(bitString: String): S =
        fromBooleanList(BitStringEncoder.decode(bitString))

    protected abstract fun fromBooleanList(booleanList: List<Boolean>): S

    protected fun from(booleanList: List<Boolean>): Set<P> = values
        .filterIndexed { i, _ -> booleanList.getOrNull(i) == true }
        .toSet()
  }

  /**
   * Must return whether or not the instance contains the given permission.
   *
   * The implementation should typically look like:
   * override fun contains(permission: Permission): Boolean = permission in permissions
   */
  abstract operator fun contains(permission: Permission): Boolean

  /**
   * Must return all permissions in order. The order used here is the order used for encoding and
   * serialization, so it must be consistent. If the permission class has n permissions, a list of
   * length n is expected, in ascending order of bits.
   *
   * The implementation should typically look like:
   * override fun asBooleanList(): List<Boolean> = values.map { it in permissions }
   */
  protected abstract fun asBooleanList(): List<Boolean>

  open fun asDarb(): String = DarbEncoder.encode(asBooleanList())

  open fun asBitString(): String = BitStringEncoder.encode(asBooleanList())
}
