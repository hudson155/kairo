package kairo.id

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

public data class KairoIdConfig(
  val generator: Generator,
) {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(Generator.Deterministic::class, name = "Deterministic"),
    JsonSubTypes.Type(Generator.Random::class, name = "Random"),
  )
  public sealed class Generator {
    public data class Deterministic(
      override val length: Int,
    ) : Generator()

    public data class Random(
      override val length: Int,
    ) : Generator()

    public abstract val length: Int
  }
}
