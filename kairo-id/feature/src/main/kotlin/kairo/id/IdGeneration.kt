package kairo.id

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class IdGeneration {
  @Serializable
  @SerialName("Random")
  public data class Random(
    val length: Int,
  ) : IdGeneration()
}
