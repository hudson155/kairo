package io.limberapp.common.permissions

interface Permission {
  /**
   * Sets of permissions are represented by bit lists, not by maps. The bit index represents which
   * bit in the list corresponds to the permission. It's critical that bits increase monotonically
   * with the addition of each new permission, and that they are stable (don't change over time).
   */
  val index: Int

  /**
   * The title is directly visible to the user on the frontend.
   */
  val title: String

  /**
   * The description is also directly visible to the user on the frontend.
   */
  val description: String
}
