package io.limberapp.backend.authorization.permissions

import com.piperframework.util.darb.BitStringEncoder
import com.piperframework.util.darb.DarbEncoder

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

  fun hasPermission(permission: P) = permission in permissions

  private fun asDarbWithPrefix() = listOfNotNull(prefix, asDarb()).joinToString(".")

  private fun asDarb() = DarbEncoder.encode(asBooleanList())

  fun asBitString() = BitStringEncoder.encode(asBooleanList())

  final override fun toString() = asDarbWithPrefix()
}
