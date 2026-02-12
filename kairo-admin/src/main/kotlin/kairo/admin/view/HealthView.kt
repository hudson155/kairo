package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.HealthCheckResult
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.p
import kotlinx.html.span

@Suppress("LongMethod")
internal fun FlowContent.healthView(
  config: AdminDashboardConfig,
  results: List<HealthCheckResult>,
  hasChecks: Boolean,
) {
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6")
    +"Health Checks"
  }
  if (!hasChecks) {
    p {
      classes = setOf("text-gray-500")
      +"No health checks configured."
    }
    return
  }
  div {
    classes = setOf("mb-4")
    form(action = "${config.pathPrefix}/health/run", method = FormMethod.post) {
      button(type = ButtonType.submit) {
        classes = setOf("px-6", "py-2", "bg-blue-600", "text-white", "rounded-md", "hover:bg-blue-700", "font-medium")
        +"Run All Checks"
      }
    }
  }
  if (results.isNotEmpty()) {
    div {
      classes = setOf("space-y-3")
      results.forEach { result ->
        healthCheckCard(result)
      }
    }
  }
}

@Suppress("LongMethod")
private fun FlowContent.healthCheckCard(result: HealthCheckResult) {
  div {
    classes = setOf("bg-white", "rounded-xl", "shadow-md", "p-5")
    div {
      classes = setOf("flex", "items-center", "gap-3")
      span {
        classes = if (result.passed) {
          setOf("px-2", "py-1", "text-xs", "bg-green-100", "text-green-800", "rounded-full", "font-medium")
        } else {
          setOf("px-2", "py-1", "text-xs", "bg-red-100", "text-red-800", "rounded-full", "font-medium")
        }
        +if (result.passed) "PASS" else "FAIL"
      }
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900")
        +result.name
      }
      span {
        classes = setOf("text-sm", "text-gray-400", "ml-auto")
        +"${result.durationMs}ms"
      }
    }
    if (result.error != null) {
      div {
        classes = setOf(
          "mt-3",
          "bg-red-50",
          "border",
          "border-red-200",
          "text-red-800",
          "px-4",
          "py-3",
          "rounded-lg",
          "text-sm",
          "font-mono",
        )
        +result.error
      }
    }
  }
}
