package limber.rest.ktorPlugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.util.AttributeKey
import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime

private val callStartTime = AttributeKey<ZonedDateTime>("limber.ktor.attribute.callStartTime")

private val clock: Clock = Clock.systemUTC()

internal fun Application.installMonitoringPlugins() {
  install(CallLogging) {
    mdc("responseTime") { call ->
      val duration = Duration.between(call.attributes[callStartTime], ZonedDateTime.now(clock))
      return@mdc duration.toMillis().toString()
    }
  }.also {
    intercept(ApplicationCallPipeline.Setup) {
      call.attributes.put(callStartTime, ZonedDateTime.now(clock))
    }
  }
}
