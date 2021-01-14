package io.limberapp.common.validation

internal object Regex {
  val auth0ClientId = Regex("[A-Za-z0-9]{32}")

  val base64EncodedUuid = Regex("[A-Za-z0-9+/]{21}[AQgw]==")

  val hostname = run {
    val portion = Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])")
    return@run Regex("$portion?(?:\\.$portion?)+")
  }

  val emailAddress = Regex("[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+@$hostname")

  private val urlChar = Regex("[A-Za-z0-9\\-_.%]")

  val path = Regex("(?:/$urlChar+)*/?")

  val url = run {
    val protocol = Regex("[Hh][Tt][Tt][Pp][Ss]?://")
    val queryString =
        Regex("(?:\\?$urlChar+=$urlChar*(?:&$urlChar+=$urlChar*)*)?")
    val hash = Regex("(?:#$urlChar*)?")
    return@run Regex(listOf(protocol, hostname, path, queryString, hash).joinToString(""))
  }

  private val hex = Regex("[A-Fa-f0-9]")

  val uuid = Regex("$hex{8}-?$hex{4}-?$hex{4}-?$hex{4}-?$hex{12}")
}
