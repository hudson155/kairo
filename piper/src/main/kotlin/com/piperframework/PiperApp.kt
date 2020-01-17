package com.piperframework

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.ApplicationStopping
import org.slf4j.LoggerFactory

abstract class PiperApp<Context : Any>(application: Application) {

    private val logger = LoggerFactory.getLogger(PiperApp::class.java)

    private var context: Context? = null

    init {
        application.environment.monitor.subscribe(ApplicationStarted, ::onStartInternal)
        application.environment.monitor.subscribe(ApplicationStopping, ::onStopInternal)
    }

    private fun onStartInternal(application: Application) {
        logger.info("PiperApp starting...")
        check(context == null)
        context = onStart(application)
    }

    private fun onStopInternal(application: Application) {
        logger.info("PiperApp stopping...")
        context?.let { onStop(application, it) }
        context = null
    }

    abstract fun onStart(application: Application): Context

    abstract fun onStop(application: Application, context: Context)
}
