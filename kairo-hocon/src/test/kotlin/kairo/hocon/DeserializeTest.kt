package kairo.hocon

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.typesafe.config.ConfigFactory
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kairo.serialization.KairoJson
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This single test covers several use cases: see inline comments.
 * This is only a happy-path test.
 */
@OptIn(ProtectedString.Access::class)
internal class DeserializeTest {
  internal data class Config(
    val stringFromCommon: String, // This string comes from common.conf.
    val stringFromTest: String, // This string comes from test.conf.
    val overriddenString: String, // This string is present in both files.
    val nested: Nested, // Tests nested structures.
    val nullEnvironmentVariable: String?, // Tests a missing environment variable.
    val environmentVariable: String, // Tests an environment variable.
    val duration: Duration,
    val list: List<Int>,
    val map: Map<String, String>,
    val animal: Animal,
    val protectedString: ProtectedString,
  ) {
    internal data class Nested(
      val intFromCommon: Int, // This int comes from common.conf.
      val intFromTest: Int, // This int comes from test.conf.
      val overriddenInt: Int, // This int is present in both files.
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
      JsonSubTypes.Type(Animal.Dog::class, name = "Dog"),
      JsonSubTypes.Type(Animal.Cat::class, name = "Cat"),
    )
    internal sealed class Animal {
      abstract val name: String

      internal data class Dog(override val name: String, val barksPerMinute: Int) : Animal()

      internal data class Cat(override val name: String, val napsPerDay: Int) : Animal()
    }
  }

  private val json: KairoJson = KairoJson()

  @Test
  fun test(): Unit =
    runTest {
      val hocon = ConfigFactory.parseResources("config/test.conf").resolve()
      val config = json.deserialize<Config>(hocon)
      config.shouldBe(
        Config(
          stringFromCommon = "It's recommended but not required to quote strings.",
          stringFromTest = "This string is quoted",
          overriddenString = "Good morning!",
          nested = Config.Nested(
            intFromCommon = 42,
            intFromTest = 1337,
            overriddenInt = 12_345,
          ),
          nullEnvironmentVariable = null,
          environmentVariable = "Hello, World!",
          duration = 5.seconds,
          list = listOf(1, 2, 3),
          map = mapOf("key0" to "common", "key1" to "test"),
          animal = Config.Animal.Dog("Rex", 30),
          protectedString = ProtectedString("supersecretvalue"),
        ),
      )
    }
}
