package io.limberapp.common

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.ApplicationStopPreparing
import io.ktor.application.ApplicationStopping
import io.ktor.server.engine.ApplicationEngineEnvironment
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * A barebones Limber application. [Context] is the context of the running application, which is only non-null while the
 * app is running. Unless an application needs heavy customization, it's generally easiest to extend from
 * [SimpleLimberApp] this rather than this.
 */
abstract class LimberApp<Context : Any>(application: Application) {
  private val logger = LoggerFactory.getLogger(LimberApp::class.java)

  private var context: Context? = null

  init {
    application.environment.monitor.subscribe(ApplicationStarted, ::onStartInternal)
    application.environment.monitor.subscribe(ApplicationStopping, ::onStopInternal)
  }

  private fun onStartInternal(application: Application) {
    logger.info("LimberApp starting...")
    @Suppress("TooGenericExceptionCaught")
    try {
      check(context == null)
      val newContext = onStart(application)
      context = newContext
      application.afterStart(newContext)
    } catch (e: Throwable) {
      logger.error("An error occurred during application startup. Shutting down...", e)
      application.shutDown(1)
    }
  }

  private fun onStopInternal(application: Application) {
    logger.info("LimberApp stopping...")
    context?.let { onStop(application, it) }
    context = null
  }

  abstract fun onStart(application: Application): Context

  protected open fun Application.afterStart(context: Context) {}

  abstract fun onStop(application: Application, context: Context)
}

/**
 * Implementation adapted from [io.ktor.server.engine.ShutDownUrl].
 */
fun Application.shutDown(status: Int) {
  val latch = CountDownLatch(1)
  thread {
    @Suppress("MagicNumber")
    latch.await(10L, TimeUnit.SECONDS)
    environment.monitor.raise(ApplicationStopPreparing, environment)
    (environment as? ApplicationEngineEnvironment)?.stop() ?: dispose()
    exitProcess(status)
  }
  latch.countDown()
}
