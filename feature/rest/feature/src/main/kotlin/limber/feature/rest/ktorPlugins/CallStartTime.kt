package limber.feature.rest.ktorPlugins

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationPlugin
import io.ktor.server.application.createApplicationPlugin
import io.ktor.util.AttributeKey
import java.time.Clock
import java.time.ZonedDateTime

private val callStartTimeKey =
  AttributeKey<ZonedDateTime>(
    name = "limber.ktor.attribute.callStartTime",
  )

internal var ApplicationCall.startTime: ZonedDateTime
  get() = attributes[callStartTimeKey]
  set(value) = attributes.put(callStartTimeKey, value)

private val clock: Clock = Clock.systemDefaultZone()

@Suppress("TopLevelPropertyNaming") // This matches the Ktor plugin naming conventions.
internal val CallStartTime: ApplicationPlugin<Unit> = createApplicationPlugin("CallStartTime") {
  onCall { call ->
    call.startTime = ZonedDateTime.now(clock)
  }
}
