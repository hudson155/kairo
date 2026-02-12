package kairo.admin.view

import kairo.admin.AdminConfigSource
import kairo.admin.AdminDashboardConfig
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p
import kotlinx.html.pre

@Suppress("LongMethod")
internal fun FlowContent.configView(
  @Suppress("UnusedParameter") config: AdminDashboardConfig,
  sources: List<AdminConfigSource>,
  effectiveConfig: String?,
) {
  pageHeader(
    "Config",
    "View HOCON configuration sources loaded by the server." +
      " Shows individual config files and the merged effective configuration. Values are read-only.",
  )
  if (sources.isEmpty() && effectiveConfig == null) {
    p {
      classes = setOf("text-gray-500")
      +"No config sources registered."
    }
    return
  }
  div {
    classes = setOf("space-y-8")
    // Effective Config section.
    if (effectiveConfig != null) {
      div {
        h2 {
          classes = setOf("text-xl", "font-semibold", "text-gray-900", "mb-3")
          +"Effective Config"
        }
        pre {
          classes = setOf(
            "bg-gray-100",
            "text-gray-900",
            "p-4",
            "rounded-lg",
            "overflow-auto",
            "text-sm",
            "font-mono",
          )
          +effectiveConfig
        }
      }
    }
    // Individual config source files.
    sources.forEach { source ->
      div {
        h2 {
          classes = setOf("text-xl", "font-semibold", "text-gray-900", "mb-3")
          +source.name
        }
        pre {
          classes = setOf(
            "bg-gray-100",
            "text-gray-900",
            "p-4",
            "rounded-lg",
            "overflow-auto",
            "text-sm",
            "font-mono",
          )
          +source.content
        }
      }
    }
  }
}
