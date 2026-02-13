package kairo.admin.collector

import java.util.logging.Level
import java.util.logging.LogManager
import kairo.admin.model.LoggerInfo

internal class LoggingCollector {
  fun listLoggers(): List<LoggerInfo> =
    try {
      listLog4j2Loggers()
    } catch (_: Exception) {
      listJulLoggers()
    }

  fun setLevel(name: String, level: String) {
    try {
      setLog4j2Level(name, level)
    } catch (_: Exception) {
      setJulLevel(name, level)
    }
  }

  private fun listLog4j2Loggers(): List<LoggerInfo> {
    val contextClass = Class.forName("org.apache.logging.log4j.core.LoggerContext")
    val getContext = Class.forName("org.apache.logging.log4j.LogManager")
      .getMethod("getContext", Boolean::class.javaPrimitiveType)
    val context = getContext.invoke(null, false)
    val getLoggers = contextClass.getMethod("getLoggers")
    @Suppress("UNCHECKED_CAST")
    val loggers = getLoggers.invoke(context) as Collection<Any>
    return loggers.map { logger ->
      val getName = logger::class.java.getMethod("getName")
      val getLevel = logger::class.java.getMethod("getLevel")
      val loggerName = getName.invoke(logger) as String
      val loggerLevel = getLevel.invoke(logger)?.toString()
      LoggerInfo(name = loggerName, level = loggerLevel)
    }.sortedBy { it.name }
  }

  @Suppress("SwallowedException")
  private fun setLog4j2Level(name: String, level: String) {
    val configurator = Class.forName("org.apache.logging.log4j.core.config.Configurator")
    val levelClass = Class.forName("org.apache.logging.log4j.Level")
    val toLevel = levelClass.getMethod("toLevel", String::class.java)
    val log4jLevel = toLevel.invoke(null, level)
    val setLevel = configurator.getMethod("setLevel", String::class.java, levelClass)
    setLevel.invoke(null, name, log4jLevel)
  }

  private fun listJulLoggers(): List<LoggerInfo> {
    val manager = LogManager.getLogManager()
    return manager.loggerNames.toList().sorted().map { name ->
      val logger = manager.getLogger(name)
      LoggerInfo(name = name, level = logger?.level?.name)
    }
  }

  private fun setJulLevel(name: String, level: String) {
    val manager = LogManager.getLogManager()
    val logger = manager.getLogger(name) ?: return
    logger.level = Level.parse(level)
  }
}
