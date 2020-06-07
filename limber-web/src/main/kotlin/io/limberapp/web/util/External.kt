package io.limberapp.web.util

internal interface Process {
  @Suppress("PropertyName", "VariableNaming") // Environment variables are typically in UPPER_SNAKE_CASE.
  interface Env {
    val API_ROOT_URL: String
    val AUTH0_DOMAIN: String
    val COPYRIGHT_HOLDER: String
    val SEGMENT_WRITE_KEY: String?
  }

  val env: Env
}

internal external val process: Process
