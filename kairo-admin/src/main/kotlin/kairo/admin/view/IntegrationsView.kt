package kairo.admin.view

import kairo.admin.model.AdminIntegrationInfo
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.tr

internal fun FlowContent.integrationsView(integrations: List<AdminIntegrationInfo>) {
  pageHeader(
    "Integrations",
    "Auto-detected external service integrations and their current connection status.",
  )
  if (integrations.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No integrations configured."
    }
    return
  }
  div {
    classes = setOf("grid", "grid-cols-1", "md:grid-cols-2", "gap-6")
    integrations.forEach { integration ->
      integrationCard(integration)
    }
  }
}

@Suppress("LongMethod")
private fun FlowContent.integrationCard(integration: AdminIntegrationInfo) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-5")
    div {
      classes = setOf("flex", "items-center", "gap-3", "mb-3")
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900")
        +integration.name
      }
      span {
        classes = when (integration.status) {
          "connected" -> setOf("px-2", "py-1", "text-xs", "bg-green-50", "text-green-700", "rounded-full")
          "error" -> setOf("px-2", "py-1", "text-xs", "bg-red-50", "text-red-700", "rounded-full")
          else -> setOf("px-2", "py-1", "text-xs", "bg-gray-100", "text-gray-700", "rounded-full")
        }
        +integration.status
      }
    }
    span {
      classes = setOf("text-sm", "text-gray-500", "mb-3", "block")
      +integration.type
    }
    if (integration.details.isNotEmpty()) {
      table {
        classes = setOf("w-full", "text-sm", "table-fixed")
        tbody {
          integration.details.forEach { (key, value) ->
            tr {
              td {
                classes = setOf("py-1", "pr-4", "text-gray-500", "font-medium", "whitespace-nowrap", "w-48")
                +key
              }
              td {
                classes = setOf("py-1", "text-gray-900", "font-mono", "text-xs", "break-all")
                +value
              }
            }
          }
        }
      }
    }
  }
}
