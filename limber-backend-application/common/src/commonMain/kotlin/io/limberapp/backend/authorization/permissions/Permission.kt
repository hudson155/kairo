package io.limberapp.backend.authorization.permissions

interface Permission {
  val bit: Int
  val title: String
  val description: String
}
