package kairo.serialization.module.money

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to money serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to monies.
 */
internal class MoneyNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Money?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, positive`(): Unit = runTest {
    mapper.kairoWrite(MyClass(Money.of(12_345.67, "USD")))
      .shouldBe("{\"value\":{\"amount\":\"12345.67\",\"currency\":\"USD\"}}")
  }

  @Test
  fun `serialize, zero`(): Unit = runTest {
    mapper.kairoWrite(MyClass(Money.of(0, "USD")))
      .shouldBe("{\"value\":{\"amount\":\"0.00\",\"currency\":\"USD\"}}")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.kairoWrite(MyClass(Money.of(-12_345.67, "USD")))
      .shouldBe("{\"value\":{\"amount\":\"-12345.67\",\"currency\":\"USD\"}}")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": { \"amount\": \"12345.67\", \"currency\": \"USD\" } }")
      .shouldBe(MyClass(Money.of(12_345.67, "USD")))
  }

  @Test
  fun `deserialize, zero`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": { \"amount\": \"0.00\", \"currency\": \"USD\" } }")
      .shouldBe(MyClass(Money.of(0, "USD")))
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": { \"amount\": \"-12345.67\", \"currency\": \"USD\" } }")
      .shouldBe(MyClass(Money.of(-12_345.67, "USD")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{}")
      .shouldBe(MyClass(null))
  }
}
