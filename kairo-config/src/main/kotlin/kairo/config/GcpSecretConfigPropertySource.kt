package kairo.config

import kairo.config.GcpSecretConfigPropertySource.ConfigProperty
import kairo.gcpSecretSupplier.DefaultGcpSecretSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier
import kairo.protectedString.ProtectedString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class GcpSecretConfigPropertySource(
  private val gcpSecretSupplier: GcpSecretSupplier = DefaultGcpSecretSupplier(),
) : ConfigPropertySource<ConfigProperty>() {
  @Serializable
  @SerialName("GcpSecret")
  public data class ConfigProperty(
    val id: String,
  ) : ConfigPropertySource.ConfigProperty()

  override val serializer: KSerializer<ConfigProperty> = ConfigProperty.serializer()

  @OptIn(ProtectedString.Access::class)
  override suspend fun resolve(property: ConfigProperty): String? =
    gcpSecretSupplier[property.id]?.value
}
