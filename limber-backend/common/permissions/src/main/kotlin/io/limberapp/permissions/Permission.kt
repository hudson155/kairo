package io.limberapp.permissions

interface Permission {
  val bit: Int
  val title: String
  val description: String
}
