package io.limberapp.common.permissions

interface Permission {
  val bit: Int
  val title: String
  val description: String
}
