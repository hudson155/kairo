package com.piperframework.restInterface

enum class ContentType(val headerValue: String) {
  CSV("text/csv"),
  JSON("application/json");
}
