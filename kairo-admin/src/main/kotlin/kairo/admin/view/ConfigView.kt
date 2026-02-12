package kairo.admin.view

import kairo.admin.AdminConfigSource
import kairo.admin.AdminDashboardConfig
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.pre

@Suppress("LongMethod")
internal fun FlowContent.configView(
  config: AdminDashboardConfig,
  sources: List<AdminConfigSource>,
  selectedName: String?,
) {
  h1 {
    classes = setOf("text-2xl", "font-semibold", "text-gray-900", "mb-6")
    +"Config"
  }
  if (sources.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No config sources registered."
    }
    return
  }
  div {
    classes = setOf("flex", "gap-6")
    // Sidebar: config file tabs.
    div {
      classes = setOf("w-48", "flex-shrink-0", "space-y-1")
      sources.forEach { source ->
        val isActive = source.name == selectedName
        a(href = "${config.pathPrefix}/config/${source.name}") {
          classes = if (isActive) {
            setOf("block", "px-3", "py-2", "rounded-md", "bg-indigo-50", "text-indigo-700", "font-medium", "text-sm")
          } else {
            setOf("block", "px-3", "py-2", "rounded-md", "text-gray-600", "hover:bg-gray-100", "text-sm")
          }
          +source.name
        }
      }
    }
    // Content: selected config file.
    div {
      classes = setOf("flex-1", "bg-white", "rounded-lg", "shadow-sm", "p-6")
      val selected = sources.find { it.name == selectedName }
      if (selected != null) {
        pre {
          classes = setOf("bg-gray-100", "text-gray-900", "p-4", "rounded-lg", "overflow-auto", "text-sm", "font-mono")
          +selected.content
        }
      } else {
        p {
          classes = setOf("text-gray-500")
          +"Select a config file."
        }
      }
    }
  }
}
