package kairo.rest.util

import io.ktor.server.request.ApplicationRequest
import kairo.rest.exception.DuplicateHeader

public fun ApplicationRequest.headerSingleNullOrThrow(name: String): String? {
  val headers = headers.getAll(name).orEmpty()
  if (headers.isEmpty()) return null
  if (headers.size > 1) throw DuplicateHeader(name)
  return headers.single()
}
