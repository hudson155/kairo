package limber.type

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.Optional

internal object OptionalWrapper {
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class NonNullable(val optional: Optional<String>)

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  internal data class Nullable(val optional: Optional<String>?)
}
