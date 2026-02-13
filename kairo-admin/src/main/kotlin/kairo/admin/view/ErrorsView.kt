package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.ErrorRecord
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.details
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.summary

@Suppress("LongMethod")
internal fun FlowContent.errorsView(config: AdminDashboardConfig, errors: List<ErrorRecord>) {
  pageHeader(
    "Errors",
    "Recent server errors captured at runtime." +
      " Shows error type, status code, message, and stack trace." +
      " Errors are stored in memory and can be cleared.",
  )
  if (errors.isNotEmpty()) {
    div {
      classes = setOf("mb-4")
      form(action = "${config.pathPrefix}/errors/clear", method = FormMethod.post) {
        button(type = ButtonType.submit) {
          classes = setOf(
            "px-4",
            "py-2",
            "text-sm",
            "bg-red-50",
            "text-red-700",
            "rounded-md",
            "hover:bg-red-100",
            "font-medium",
          )
          +"Clear All (${errors.size})"
        }
      }
    }
  }
  if (errors.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No errors recorded."
    }
    return
  }
  div {
    classes = setOf("space-y-3")
    errors.forEach { error ->
      errorCard(error)
    }
  }
}

private fun FlowContent.errorCard(error: ErrorRecord) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-5")
    div {
      classes = setOf("flex", "items-center", "gap-3", "mb-2")
      error.statusCode?.let { code ->
        span {
          classes = setOf("px-2", "py-1", "text-xs", "bg-red-50", "text-red-700", "rounded-full", "font-mono")
          +"$code"
        }
      }
      span {
        classes = setOf("font-semibold", "text-gray-900", "text-sm")
        +error.type
      }
      span {
        classes = setOf("text-xs", "text-gray-400", "ml-auto")
        +error.timestamp
      }
    }
    p {
      classes = setOf("text-sm", "text-gray-700", "mb-2")
      +error.message
    }
    if (error.stackTrace != null) {
      details {
        summary {
          classes = setOf("text-xs", "text-gray-500", "cursor-pointer")
          +"Stack trace"
        }
        pre {
          classes = setOf("mt-2", "text-xs", "font-mono", "bg-gray-50", "p-3", "rounded-lg", "overflow-auto")
          +error.stackTrace
        }
      }
    }
  }
}
