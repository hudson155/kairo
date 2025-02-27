package kairo.transactionManager

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Extension function that checks for transaction context before executing a suspending function.
 */
public suspend fun <T> ensureTransaction(block: suspend () -> T): T {
  getTransactionContext() ?: run {
    logger.error { "Transaction context is missing" }
    throw MissingTransactionException()
  }
  return block()
}
