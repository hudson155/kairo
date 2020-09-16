package io.limberapp.common.restInterface

enum class ContentType(val headerValue: String) {
  CSV("text/csv"),
  JSON("application/json");
}
