package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.collector.JvmCollector
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.tr

internal fun FlowContent.jvmView(config: AdminDashboardConfig, jvmCollector: JvmCollector) {
  pageHeader(
    "JVM",
    "Live JVM metrics including memory usage, thread counts, runtime info, and garbage collector statistics." +
      " Auto-refreshes every 5 seconds.",
  )
  div {
    attributes["data-controller"] = "refresh"
    attributes["data-refresh-url-value"] = "${config.pathPrefix}/jvm/refresh"
    attributes["data-refresh-interval-value"] = "5000"
    jvmStatsContent(jvmCollector)
  }
}

internal fun HTML.jvmStatsPartial(jvmCollector: JvmCollector) {
  body {
    jvmStatsContent(jvmCollector)
  }
}

private fun FlowContent.jvmStatsContent(jvmCollector: JvmCollector) {
  div {
    classes = setOf("grid", "grid-cols-1", "lg:grid-cols-2", "gap-6")
    statsCard("Memory", jvmCollector.collectMemory())
    statsCard("Threads", jvmCollector.collectThreads())
    statsCard("Runtime", jvmCollector.collectRuntime())
    statsCard("System", jvmCollector.collectSystem())
  }
  val gcList = jvmCollector.collectGc()
  if (gcList.isNotEmpty()) {
    div {
      classes = setOf("mt-6")
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-3")
        +"Garbage Collectors"
      }
      div {
        classes = setOf("grid", "grid-cols-1", "md:grid-cols-2", "gap-4")
        gcList.forEach { gc ->
          statsCard(gc["Name"] ?: "GC", gc.filterKeys { it != "Name" })
        }
      }
    }
  }
}

private fun FlowContent.statsCard(title: String, data: Map<String, String>) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-5")
    h3 {
      classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-3")
      +title
    }
    table {
      classes = setOf("w-full", "text-sm", "table-fixed")
      tbody {
        data.forEach { (key, value) ->
          tr {
            td {
              classes = setOf("py-1", "pr-4", "text-gray-500", "font-medium", "whitespace-nowrap", "w-48")
              +key
            }
            td {
              classes = setOf("py-1", "text-gray-900", "font-mono", "break-all")
              +value
            }
          }
        }
      }
    }
  }
}
