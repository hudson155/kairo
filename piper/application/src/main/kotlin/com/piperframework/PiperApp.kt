package com.piperframework

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.ApplicationStopPreparing
import io.ktor.application.ApplicationStopping
import io.ktor.server.engine.ApplicationEngineEnvironment
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * A barebones Piper application. [Context] is the context of the running application, which is only non-null while the
 * app is running. Unless an application needs heavy customization, it's generally easiest to extend from
 * [SimplePiperApp] this rather than this.
 */
abstract class PiperApp<Context : Any>(application: Application) {
  private val logger = LoggerFactory.getLogger(PiperApp::class.java)

  private var context: Context? = null

  init {
    application.environment.monitor.subscribe(ApplicationStarted, ::onStartInternal)
    application.environment.monitor.subscribe(ApplicationStopping, ::onStopInternal)
  }

  private fun onStartInternal(application: Application) {
    logger.info("PiperApp starting...")
    @Suppress("TooGenericExceptionCaught")
    try {
      check(context == null)
      context = onStart(application)
    } catch (e: Throwable) {
      logger.error("An error occurred during application startup. Shutting down...", e)
      application.shutDown()
    }
  }

  private fun onStopInternal(application: Application) {
    logger.info("PiperApp stopping...")
    context?.let { onStop(application, it) }
    context = null
  }

  abstract fun onStart(application: Application): Context

  abstract fun onStop(application: Application, context: Context)

  /**
   * Implementation adapted from [io.ktor.server.engine.ShutDownUrl].
   */
  private fun Application.shutDown() {
    val latch = CountDownLatch(1)
    thread {
      @Suppress("MagicNumber")
      latch.await(10L, TimeUnit.SECONDS)
      environment.monitor.raise(ApplicationStopPreparing, environment)
      (environment as? ApplicationEngineEnvironment)?.stop() ?: dispose()
      exitProcess(1)
    }
    latch.countDown()
  }
}
