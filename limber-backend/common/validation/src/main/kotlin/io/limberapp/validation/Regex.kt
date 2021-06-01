package io.limberapp.validation

import kotlin.text.Regex

internal object Regex {
  val auth0ClientId: Regex = Regex("org_[A-Za-z0-9]{16}")

  val base64EncodedUuid: Regex = Regex("[A-Za-z0-9+/]{21}[AQgw]==")

  val hostname: Regex = run {
    val portion = Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])")
    return@run Regex("$portion?(?:\\.$portion?)+")
  }

  val emailAddress: Regex = Regex("[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname")

  private val urlChar: Regex = Regex("[A-Za-z0-9\\-_.%?=#]")

  val path: Regex = Regex("(?:/$urlChar+)*/?")

  val url: Regex = run {
    val protocol = Regex("[Hh][Tt][Tt][Pp][Ss]?://")
    return@run Regex(listOf(protocol, hostname, path).joinToString(""))
  }

  private val hex: Regex = Regex("[A-Fa-f0-9]")

  val uuid: Regex = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}")
}
