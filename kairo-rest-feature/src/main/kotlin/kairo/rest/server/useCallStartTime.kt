package kairo.rest.server

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.util.AttributeKey
import java.time.Clock
import java.time.Instant

private val callStartTimeKey: AttributeKey<Instant> =
  AttributeKey("kairo.callStartTime")

internal var ApplicationCall.startTime: Instant
  get() = attributes[callStartTimeKey]
  set(value) = attributes.put(callStartTimeKey, value)

private val clock: Clock = Clock.systemDefaultZone()

internal fun Application.useCallStartTime() {
  intercept(ApplicationCallPipeline.Setup) {
    call.attributes.put(callStartTimeKey, Instant.now(clock))
  }
}
