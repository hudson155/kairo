package kairo.config

import kairo.config.EnvironmentVariableConfigPropertySource.Property
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class EnvironmentVariableConfigPropertySource(
  private val environmentVariableSupplier: EnvironmentVariableSupplier = DefaultEnvironmentVariableSupplier(),
) : ConfigPropertySource<Property>() {
  @Serializable
  @SerialName("EnvironmentVariable")
  public data class Property(
    val name: String,
    val default: String? = null,
  ) : ConfigProperty()

  override val serializer: KSerializer<Property> = Property.serializer()

  override suspend fun resolve(property: Property): String? =
    environmentVariableSupplier[property.name] ?: property.default
}
