package io.limberapp.common.logging

import kotlin.test.Test

/**
 * This simple test just makes sure there are no exceptions thrown.
 */
internal class LoggerFactoryTest {
  @Test
  fun log() {
    val logger = LoggerFactory.getLogger(LoggerFactoryTest::class)
    logger.debug("Debug message.")
    logger.info("Info message.")
    logger.warn("Warn message.")
    logger.error("Error message.")
  }
}
