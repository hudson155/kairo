package kairo.config

import kairo.config.EnvironmentVariableConfigPropertySource.ConfigProperty
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class EnvironmentVariableConfigPropertySource(
  private val environmentVariableSupplier: EnvironmentVariableSupplier = DefaultEnvironmentVariableSupplier(),
) : ConfigPropertySource<ConfigProperty>() {
  @Serializable
  @SerialName("EnvironmentVariable")
  public data class ConfigProperty(
    val name: String,
    val default: String? = null,
  ) : ConfigPropertySource.ConfigProperty()

  override val serializer: KSerializer<ConfigProperty> = ConfigProperty.serializer()

  override suspend fun resolve(property: ConfigProperty): String? =
    environmentVariableSupplier[property.name] ?: property.default
}
