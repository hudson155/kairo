package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.serialization.module.configurePrettyPrinting
import kairo.serialization.module.increaseStrictness
import kairo.serialization.module.java.configureJava
import kairo.serialization.module.kotlin.configureKotlin
import kairo.serialization.module.primitives.StringDeserializer
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.module.primitives.configurePrimitives
import kairo.serialization.module.setUnknownPropertyHandling
import kairo.serialization.module.time.configureTime

private val logger: KLogger = KotlinLogging.logger {}

public class ObjectMapperFactoryBuilder internal constructor(
  factory: JsonFactory,
  modules: List<Module>,
  block: ObjectMapperFactoryBuilder.() -> Unit,
) : JsonMapper.Builder(JsonMapper(factory)) {
  /**
   * Unknown properties are prohibited by default by Jackson, and we respect that default here.
   * This is an appropriate choice for internal use.
   * However, it's not an appropriate choice for object mappers that communicate with 3rd-party APIs.
   */
  public var allowUnknownProperties: Boolean = false

  /**
   * Pretty printing usually isn't desirable since it creates longer output,
   * but it can sometimes be nice.
   */
  public var prettyPrint: Boolean = false

  /**
   * See [TrimWhitespace] and [StringDeserializer].
   */
  public var trimWhitespace: TrimWhitespace.Type = TrimWhitespace.Type.TrimNone

  init {
    block()

    configureKotlin()
    configureJava()
    configurePrimitives(this)
    configureTime()

    increaseStrictness()
    configurePrettyPrinting(prettyPrint = prettyPrint)
    setUnknownPropertyHandling()

    addModules(modules)
  }

  override fun build(): JsonMapper {
    logger.debug { "Creating object mapper." }
    return super.build()
  }
}
