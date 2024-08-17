package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

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

  init {
    block()

    configureKotlin()
    configureJava()
    configureStrings()
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
