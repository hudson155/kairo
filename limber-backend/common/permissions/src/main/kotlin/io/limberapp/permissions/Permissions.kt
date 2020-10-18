package io.limberapp.permissions

import com.fasterxml.jackson.annotation.JsonValue
import io.limberapp.common.util.darb.BitStringEncoder
import io.limberapp.common.util.darb.DarbEncoder

/**
 * Generic class to represent a set of permissions.
 */
abstract class Permissions<P : Permission> {
  /**
   * Each type of permissions needs a unique prefix so that the permissions can be understood when deserializing.
   */
  abstract val prefix: Char?

  protected abstract val permissions: Set<P>

  /**
   * Must return all permissions in order. Should do this in constant time. The order used here is the order used for
   * encoding and serialization, so it must be consistent.
   */
  protected abstract fun allPermissions(): List<P>

  private fun asBooleanList() = allPermissions().map { it in permissions }

  val size get() = permissions.size

  operator fun contains(permission: P) = permission in permissions

  @JsonValue
  fun asDarb() = listOfNotNull(prefix, DarbEncoder.encode(asBooleanList())).joinToString(".")

  fun asBitString() = listOfNotNull(prefix, BitStringEncoder.encode(asBooleanList())).joinToString("")
}
