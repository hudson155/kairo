package kairo.config

import kairo.config.PlaintextConfigPropertySource.ConfigProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class PlaintextConfigPropertySource : ConfigPropertySource<ConfigProperty>() {
  @Serializable
  @SerialName("Plaintext")
  public data class ConfigProperty(
    val value: String,
  ) : ConfigPropertySource.ConfigProperty()

  override val serializer: KSerializer<ConfigProperty> = ConfigProperty.serializer()

  override suspend fun resolve(property: ConfigProperty): String =
    property.value
}
