package kairo.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature
import com.fasterxml.jackson.databind.cfg.MapperBuilder
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.serialization.module.increaseStrictness
import kairo.serialization.module.money.MoneyModule
import kairo.serialization.module.primitives.StringDeserializer
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.module.time.TimeModule

private val logger: KLogger = KotlinLogging.logger {}

public abstract class ObjectMapperFactory<M : ObjectMapper, B : MapperBuilder<M, B>> internal constructor(
  private val modules: List<Module>,
) {
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

  public fun build(): M {
    logger.debug { "Creating object mapper." }

    return createBuilder().apply {
      configureKotlin(this)
      configureJava(this)
      configureMoney(this)
      configurePrimitives(this)
      configureTime(this)

      increaseStrictness()
      configurePrettyPrinting(this)
      setUnknownPropertyHandling(this)

      addModules(modules)
    }.build()
  }

  protected abstract fun createBuilder(): B

  /**
   * Besides just installing the Kotlin module,
   * we change a few config params to have more sensible values.
   */
  private fun configureKotlin(builder: B) {
    builder.addModule(
      kotlinModule {
        this.configure(KotlinFeature.SingletonSupport, true)
        this.configure(KotlinFeature.StrictNullChecks, true)
      },
    )
  }

  /**
   * The only thing we need to configure is support for [Optional]s,
   * which is not included in Jackson's core until 3.0.
   *
   * See the corresponding test for more spec.
   */
  private fun configureJava(builder: B) {
    builder.addModule(
      Jdk8Module().apply {
        this.configureReadAbsentAsNull(true)
      },
    )
  }

  private fun configureMoney(builder: B) {
    builder.addModule(MoneyModule())
  }

  protected abstract fun configurePrimitives(builder: B)

  private fun configureTime(builder: B) {
    builder.addModule(TimeModule())
    builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    builder.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
    builder.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
  }

  /**
   * See the corresponding test for more spec.
   */
  protected open fun configurePrettyPrinting(builder: B) {
    builder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, this.prettyPrint)
    builder.configure(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST, false)

    builder.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint)
    builder.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, this.prettyPrint)

    builder.configure(JsonNodeFeature.WRITE_PROPERTIES_SORTED, this.prettyPrint)
  }

  private fun setUnknownPropertyHandling(builder: B) {
    builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !this.allowUnknownProperties)
    builder.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, !this.allowUnknownProperties)
    builder.configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, !this.allowUnknownProperties)
  }
}
