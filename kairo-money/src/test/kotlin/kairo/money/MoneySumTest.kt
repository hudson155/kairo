package kairo.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import javax.money.MonetaryException
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class MoneySumTest {
  @Test
  fun empty(): Unit = runTest {
    emptyList<Money>().sumOrNull()
      .shouldBe(null)

    emptyList<Money>().sum("USD")
      .shouldBe(Money.of(0, "USD"))
  }

  @Test
  fun one(): Unit = runTest {
    listOf(Money.of(1.23, "USD")).sumOrNull()
      .shouldBe(Money.of(1.23, "USD"))

    listOf(Money.of(1.23, "USD")).sum("USD")
      .shouldBe(Money.of(1.23, "USD"))

    shouldThrow<MonetaryException> {
      listOf(Money.of(1.23, "USD"), Money.of(4.56, "USD"), Money.of(7.89, "USD")).sum("CAD")
    }.shouldHaveMessage("Currency mismatch: CAD/USD")
  }

  @Test
  fun multiple(): Unit = runTest {
    listOf(Money.of(1.23, "USD"), Money.of(4.56, "USD"), Money.of(7.89, "USD")).sumOrNull()
      .shouldBe(Money.of(13.68, "USD"))

    listOf(Money.of(1.23, "USD"), Money.of(4.56, "USD"), Money.of(7.89, "USD")).sum("USD")
      .shouldBe(Money.of(13.68, "USD"))

    shouldThrow<MonetaryException> {
      listOf(Money.of(1.23, "USD"), Money.of(4.56, "USD"), Money.of(7.89, "USD")).sum("CAD")
    }.shouldHaveMessage("Currency mismatch: CAD/USD")
  }
}
