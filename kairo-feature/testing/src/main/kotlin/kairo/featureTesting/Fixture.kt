package kairo.featureTesting

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.json.JsonMapper
import com.google.common.io.Resources
import kairo.reflect.typeParam
import kairo.serialization.jsonMapper
import kairo.serialization.property.prettyPrint

private val fixtureMapper: JsonMapper =
  jsonMapper {
    prettyPrint = true
  }.build()

public abstract class Fixture<out T : Any>(private val name: String) {
  private val type: JavaType = fixtureMapper.constructType(typeParam(Fixture::class, 0, this::class))

  protected operator fun get(key: Any): T {
    val resource = Resources.getResource("fixture/$name/$key.json")
    @Suppress("ForbiddenMethodCall")
    return fixtureMapper.readValue(resource, type)
  }
}
