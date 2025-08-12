package kairo.config

import kairo.config.PlaintextConfigPropertySource.Property
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class PlaintextConfigPropertySource : ConfigPropertySource<Property>() {
  @Serializable
  @SerialName("Plaintext")
  public data class Property(
    val value: String,
  ) : ConfigProperty()

  override val serializer: KSerializer<Property> = Property.serializer()

  override suspend fun resolve(property: Property): String =
    property.value
}
