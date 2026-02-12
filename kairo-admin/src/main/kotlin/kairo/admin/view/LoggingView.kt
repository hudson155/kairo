package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.LoggerInfo
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.TBODY
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.hiddenInput
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.select
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

private val logLevels: List<String> = listOf("TRACE", "DEBUG", "INFO", "WARN", "ERROR", "OFF")

@Suppress("LongMethod")
internal fun FlowContent.loggingView(config: AdminDashboardConfig, loggers: List<LoggerInfo>) {
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6")
    +"Logging"
  }
  if (loggers.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No loggers found."
    }
    return
  }
  div {
    classes = setOf("bg-white", "rounded-xl", "shadow-md", "p-6")
    table {
      classes = setOf("w-full", "text-sm")
      thead {
        tr {
          classes = setOf("border-b")
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Logger"
          }
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Level"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Change"
          }
        }
      }
      tbody {
        loggers.forEach { logger ->
          loggerRow(config, logger)
        }
      }
    }
  }
}

private fun TBODY.loggerRow(config: AdminDashboardConfig, logger: LoggerInfo) {
  tr {
    classes = setOf("border-b", "border-gray-100")
    td {
      classes = setOf("py-2", "pr-4", "font-mono", "text-xs", "break-all")
      +logger.name.ifEmpty { "(root)" }
    }
    td {
      classes = setOf("py-2", "pr-4")
      val levelText = logger.level ?: "INHERITED"
      div {
        classes = setOf("px-2", "py-1", "text-xs", "rounded-full", "font-medium", "inline-block")
        +levelText
      }
    }
    td {
      classes = setOf("py-2")
      form(action = "${config.pathPrefix}/logging/level", method = FormMethod.post) {
        classes = setOf("flex", "items-center", "gap-2")
        hiddenInput(name = "logger") { value = logger.name }
        select {
          name = "level"
          classes = setOf("text-xs", "border", "border-gray-300", "rounded-md", "px-2", "py-1")
          logLevels.forEach { level ->
            option {
              value = level
              selected = level == logger.level
              +level
            }
          }
        }
        button(type = ButtonType.submit) {
          classes = setOf("px-3", "py-1", "text-xs", "bg-blue-600", "text-white", "rounded-md", "hover:bg-blue-700")
          +"Set"
        }
      }
    }
  }
}
