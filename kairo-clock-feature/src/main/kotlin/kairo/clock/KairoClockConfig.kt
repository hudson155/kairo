package kairo.clock

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.time.ZoneId

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoClockConfig.Fixed::class, name = "Fixed"),
  JsonSubTypes.Type(KairoClockConfig.System::class, name = "System"),
)
public sealed class KairoClockConfig {
  public data class Fixed(
    val instant: Instant,
    val timeZone: ZoneId,
  ) : KairoClockConfig()

  public data class System(
    val timeZone: ZoneId,
  ) : KairoClockConfig()
}
