package kairo.config

import kairo.config.GcpSecretConfigPropertySource.Property
import kairo.gcpSecretSupplier.DefaultGcpSecretSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier
import kairo.protectedString.ProtectedString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class GcpSecretConfigPropertySource(
  private val gcpSecretSupplier: GcpSecretSupplier = DefaultGcpSecretSupplier(),
) : ConfigPropertySource<Property>() {
  @Serializable
  @SerialName("GcpSecret")
  public data class Property(
    val id: String,
  ) : ConfigProperty()

  override val serializer: KSerializer<Property> = Property.serializer()

  @OptIn(ProtectedString.Access::class)
  override suspend fun resolve(property: Property): String? =
    gcpSecretSupplier[property.id]?.value
}
