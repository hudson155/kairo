package kairo.money

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class MoneyRoundTest {
  @Test
  fun usd(): Unit =
    runTest {
      Money.of(12_345.675, "USD").round()
        .shouldBe(Money.of(12_345.68, "USD"))
      Money.of(12_345.665, "USD").round()
        .shouldBe(Money.of(12_345.66, "USD"))
      Money.of(0, "USD").round()
        .shouldBe(Money.of(0, "USD"))
      Money.of(-12_345.665, "USD").round()
        .shouldBe(Money.of(-12_345.66, "USD"))
      Money.of(-12_345.675, "USD").round()
        .shouldBe(Money.of(-12_345.68, "USD"))
    }

  @Test
  fun jpy(): Unit =
    runTest {
      Money.of(12_345.5, "JPY").round()
        .shouldBe(Money.of(12_346, "JPY"))
      Money.of(12_344.5, "JPY").round()
        .shouldBe(Money.of(12_344, "JPY"))
      Money.of(0, "JPY").round()
        .shouldBe(Money.of(0, "JPY"))
      Money.of(-12_344.5, "JPY").round()
        .shouldBe(Money.of(-12_344, "JPY"))
      Money.of(-12_345.5, "JPY").round()
        .shouldBe(Money.of(-12_346, "JPY"))
    }

  @Test
  fun tnd(): Unit =
    runTest {
      Money.of(12_345.6785, "TND").round()
        .shouldBe(Money.of(12_345.678, "TND"))
      Money.of(12_345.6775, "TND").round()
        .shouldBe(Money.of(12_345.678, "TND"))
      Money.of(0, "TND").round()
        .shouldBe(Money.of(0, "TND"))
      Money.of(-12_345.6775, "TND").round()
        .shouldBe(Money.of(-12_345.678, "TND"))
      Money.of(-12_345.6785, "TND").round()
        .shouldBe(Money.of(-12_345.678, "TND"))
    }
}
