package kairo.id

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IdFeatureConfig(
  val generation: Generation,
) {
  @Serializable
  public sealed class Generation {
    @Serializable
    @SerialName("Random")
    public data class Random(
      val length: Int,
    ) : Generation()
  }
}
