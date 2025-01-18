package kairo.hashing

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
public fun String.md5(): String {
  val md = MessageDigest.getInstance("MD5")
  val digest = md.digest(toByteArray())
  return digest.toHexString()
}
