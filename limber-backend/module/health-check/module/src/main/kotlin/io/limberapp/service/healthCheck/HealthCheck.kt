package io.limberapp.service.healthCheck

abstract class HealthCheck(internal val name: String) {
  abstract fun check()
}
