package kairo.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature
import com.fasterxml.jackson.databind.cfg.MapperBuilder
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Optional
import kairo.serialization.module.increaseStrictness
import kairo.serialization.module.ktor.KtorModule
import kairo.serialization.module.money.MoneyModule
import kairo.serialization.module.time.TimeModule
import kairo.serialization.property.allowUnknownProperties
import kairo.serialization.property.prettyPrint

private val logger: KLogger = KotlinLogging.logger {}

public abstract class ObjectMapperFactory<M : ObjectMapper, B : MapperBuilder<M, B>> {
  /**
   * Properties are stored in a weakly-typed map
   * to make this builder more extensible.
   */
  public val properties: MutableMap<String, Any> = mutableMapOf()

  public fun build(block: B.(factory: ObjectMapperFactory<M, B>) -> Unit = {}): M {
    logger.debug { "Creating object mapper." }

    return createBuilder().apply {
      configureJava(this)
      configureKotlin(this)
      configureKtor(this)
      configureMoney(this)
      configurePrimitives(this)
      configureTime(this)

      increaseStrictness()
      configurePrettyPrinting(this)
      setUnknownPropertyHandling(this)

      block(this@ObjectMapperFactory)
    }.build()
  }

  protected abstract fun createBuilder(): B

  /**
   * The only thing we need to configure is support for [Optional]s,
   * which is not included in Jackson's core until 3.0.
   *
   * See the corresponding test for more spec.
   */
  private fun configureJava(builder: B) {
    builder.addModule(
      Jdk8Module().apply {
        configureReadAbsentAsNull(true)
      },
    )
  }

  /**
   * Besides just installing the Kotlin module,
   * we change a few config params to have more sensible values.
   */
  private fun configureKotlin(builder: B) {
    builder.addModule(
      kotlinModule {
        configure(KotlinFeature.SingletonSupport, true)
        configure(KotlinFeature.NewStrictNullChecks, true)
        configure(KotlinFeature.KotlinPropertyNameAsImplicitName, true)
      },
    )
  }

  private fun configureKtor(builder: B) {
    builder.addModule(KtorModule.create())
  }

  private fun configureMoney(builder: B) {
    builder.addModule(MoneyModule.create(this))
  }

  protected abstract fun configurePrimitives(builder: B)

  private fun configureTime(builder: B) {
    builder.addModule(TimeModule.create())
    builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    builder.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
    builder.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
  }

  /**
   * See the corresponding test for more spec.
   */
  protected open fun configurePrettyPrinting(builder: B) {
    builder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, prettyPrint)
    builder.configure(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST, false)

    builder.configure(SerializationFeature.INDENT_OUTPUT, prettyPrint)
    builder.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, prettyPrint)

    builder.configure(JsonNodeFeature.WRITE_PROPERTIES_SORTED, prettyPrint)
  }

  private fun setUnknownPropertyHandling(builder: B) {
    builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !allowUnknownProperties)
    builder.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, !allowUnknownProperties)
    builder.configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, !allowUnknownProperties)
  }
}
