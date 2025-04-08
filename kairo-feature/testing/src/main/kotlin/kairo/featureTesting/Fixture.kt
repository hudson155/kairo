package kairo.featureTesting

import com.fasterxml.jackson.databind.json.JsonMapper
import com.google.common.io.Resources
import kairo.reflect.KairoType
import kairo.serialization.jsonMapper
import kairo.serialization.property.prettyPrint
import kairo.serialization.typeReference

public val fixtureMapper: JsonMapper =
  jsonMapper {
    prettyPrint = true
  }.build()

public abstract class Fixture<out T : Any>(private val name: String) {
  private val type: KairoType<T> = KairoType.from(Fixture::class, 0, this::class)

  @Suppress("ForbiddenMethodCall")
  protected operator fun get(key: Any): T {
    val resource = Resources.getResource("fixture/$name/$key.json")
    return fixtureMapper.readerFor(type.typeReference).readValue(resource)
  }
}
