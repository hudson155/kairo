package kairo.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ConfigResolverTest {
  // @Serializable
  // internal data class StringConfig(
  //   val value: String,
  // )
  //
  // @Serializable
  // internal data class NullableStringConfig(
  //   val value: String?,
  // )
  //
  // @Serializable
  // internal data class ProtectedStringConfig(
  //   val value: ProtectedString,
  // )
  //
  // @Serializable
  // internal data class NullableProtectedStringConfig(
  //   val value: ProtectedString?,
  // )
  //
  // private val key: String = "projects/012345678900/secrets/example/versions/1"
  //
  // @Test
  // fun `string, no resolver match`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("other::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = StringConfig("gcp::$key")
  //     config = configResolver.resolve(config)
  //     config.value.shouldBe("gcp::$key")
  //   }
  //
  // @Test
  // fun `string, with resolver match`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = StringConfig("gcp::$key")
  //     config = configResolver.resolve(config)
  //     config.value.shouldBe("supersecretvalue")
  //   }
  //
  // @Test
  // fun `string, with resolver match, null (not supported)`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { null },
  //       ),
  //     )
  //     var config = StringConfig("gcp::$key")
  //     shouldThrow<SerializationException> {
  //       config = configResolver.resolve(config)
  //     }
  //   }
  //
  // @Test
  // fun `string, with resolver match, null (supported)`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { null },
  //       ),
  //     )
  //     var config = NullableStringConfig("gcp::$key")
  //     config = configResolver.resolve(config)
  //     config.value.shouldBeNull()
  //   }
  //
  // @Test
  // fun `string, multiple resolver matches`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = StringConfig("gcp::$key")
  //     shouldThrow<IllegalArgumentException> {
  //       config = configResolver.resolve(config)
  //     }
  //   }
  //
  // @Test
  // fun `protected string, no resolver match`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("other::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = ProtectedStringConfig(ProtectedString("gcp::$key"))
  //     config = configResolver.resolve(config)
  //     config.value.shouldBe(ProtectedString("gcp::$key"))
  //   }
  //
  // @Test
  // fun `protected string, with resolver match`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = ProtectedStringConfig(ProtectedString("gcp::$key"))
  //     config = configResolver.resolve(config)
  //     config.value.shouldBe(ProtectedString("supersecretvalue"))
  //   }
  //
  // @Test
  // fun `protected string, with resolver match, null (not supported)`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { null },
  //       ),
  //     )
  //     var config = ProtectedStringConfig(ProtectedString("gcp::$key"))
  //     shouldThrow<SerializationException> {
  //       config = configResolver.resolve(config)
  //     }
  //   }
  //
  // @Test
  // fun `protected string, with resolver match, null (supported)`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { null },
  //       ),
  //     )
  //     var config = NullableProtectedStringConfig(ProtectedString("gcp::$key"))
  //     config = configResolver.resolve(config)
  //     config.value.shouldBeNull()
  //   }
  //
  // @Test
  // fun `protected string, multiple resolver matches`(): Unit =
  //   runTest {
  //     val configResolver = ConfigResolver(
  //       resolvers = listOf(
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //         ConfigResolver.Resolver("gcp::") { if (it == key) "supersecretvalue" else null },
  //       ),
  //     )
  //     var config = ProtectedStringConfig(ProtectedString("gcp::$key"))
  //     shouldThrow<IllegalArgumentException> {
  //       config = configResolver.resolve(config)
  //     }
  //   }
}
