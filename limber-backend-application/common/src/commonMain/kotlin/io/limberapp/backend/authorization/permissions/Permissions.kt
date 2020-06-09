package io.limberapp.backend.authorization.permissions

import com.piperframework.util.darb.BitStringEncoder
import com.piperframework.util.darb.DarbEncoder

/**
 * Generic class to represent a set of permissions.
 */
abstract class Permissions<Permission : Any> {
  protected abstract val permissions: Set<Permission>

  /**
   * Must return all permissions in order. Should do this in constant time. The order used here is the order used for
   * encoding and serialization, so it must be consistent.
   */
  protected abstract fun allPermissions(): List<Permission>

  private fun asBooleanList() = allPermissions().map { it in permissions }

  val size get() = permissions.size

  fun hasPermission(permission: Permission) = permission in permissions

  fun asDarb() = DarbEncoder.encode(asBooleanList())

  fun asBitString() = BitStringEncoder.encode(asBooleanList())

  final override fun toString() = asDarb()
}
