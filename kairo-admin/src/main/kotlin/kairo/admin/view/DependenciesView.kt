package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.AdminDependencyInfo
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.TABLE
import kotlinx.html.TBODY
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.input
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import kotlinx.html.unsafe

@Suppress("LongMethod")
internal fun FlowContent.dependenciesView(
  dependencies: List<AdminDependencyInfo>,
  config: AdminDashboardConfig,
) {
  h1 {
    classes = setOf("text-2xl", "font-semibold", "text-gray-900", "mb-6")
    +"Dependencies"
  }
  if (dependencies.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No dependency information available."
    }
    return
  }
  div {
    attributes["data-controller"] = "filter"
    // Search bar.
    div {
      classes = setOf("mb-4")
      input(type = InputType.text) {
        classes = setOf(
          "w-full",
          "p-2",
          "border",
          "border-gray-300",
          "shadow-sm",
          "rounded-md",
          "text-sm",
        )
        attributes["data-filter-target"] = "input"
        placeholder = "Search dependencies..."
      }
    }
    // Count badge.
    p {
      classes = setOf("text-sm", "text-gray-500", "mb-4")
      +"${dependencies.size} bindings registered"
    }
    // Table.
    div {
      classes = setOf("bg-white", "rounded-lg", "shadow-sm", "overflow-hidden")
      table {
        classes = setOf("w-full", "text-sm")
        thead {
          tr {
            classes = setOf("border-b", "bg-gray-50")
            th {
              classes = setOf("w-8", "py-2", "px-2")
            }
            th {
              classes = setOf("text-left", "py-2", "px-4", "font-medium", "text-gray-500")
              +"Class"
            }
            th {
              classes = setOf("text-left", "py-2", "px-4", "font-medium", "text-gray-500")
              +"Kind"
            }
            th {
              classes = setOf("text-left", "py-2", "px-4", "font-medium", "text-gray-500")
              +"Qualifier"
            }
            th {
              classes = setOf("text-left", "py-2", "px-4", "font-medium", "text-gray-500")
              +"Scope"
            }
          }
        }
        dependencies.forEach { dep ->
          dependencyRows(dep, config)
        }
      }
    }
  }
}

@Suppress("LongMethod")
private fun TABLE.dependencyRows(dep: AdminDependencyInfo, config: AdminDashboardConfig) {
  tbody {
    attributes["data-controller"] = "toggle"
    attributes["data-filter-target"] = "row"
    // Summary row (clickable).
    tr {
      classes = setOf("border-b", "border-gray-100", "cursor-pointer", "hover:bg-gray-50")
      attributes["data-action"] = "click->toggle#toggle"
      td {
        classes = setOf("py-2", "px-2", "text-center")
        span {
          attributes["data-toggle-target"] = "icon"
          attributes["style"] = "display: inline-block; transition: transform 150ms"
          unsafe { +chevronRightIcon }
        }
      }
      td {
        classes = setOf("py-2", "px-4")
        div {
          classes = setOf("font-mono", "text-xs", "break-all")
          +dep.className
        }
      }
      td {
        classes = setOf("py-2", "px-4")
        span {
          classes = setOf("px-2", "py-1", "text-xs", "rounded-full", kindBadgeColor(dep.kind))
          +dep.kind
        }
      }
      td {
        classes = setOf("py-2", "px-4", "font-mono", "text-xs")
        +(dep.qualifier ?: "-")
      }
      td {
        classes = setOf("py-2", "px-4", "font-mono", "text-xs")
        +(dep.scope ?: "root")
      }
    }
    // Detail row (hidden by default).
    tr {
      classes = setOf("hidden", "border-b", "border-gray-100")
      attributes["data-toggle-target"] = "content"
      td {
        attributes["colspan"] = "5"
        classes = setOf("p-0")
        dependencyDetail(dep, config)
      }
    }
  }
}

@Suppress("LongMethod", "CognitiveComplexMethod")
private fun FlowContent.dependencyDetail(dep: AdminDependencyInfo, config: AdminDashboardConfig) {
  div {
    classes = setOf("bg-gray-50", "p-4", "mx-4", "my-2", "rounded-lg")
    table {
      classes = setOf("w-full", "text-sm")
      tbody {
        detailRow("Package", dep.packageName.ifEmpty { "-" })
        detailRow("Class", dep.simpleName)
        detailRow("Kind", dep.kind)
        if (dep.qualifier != null) {
          detailRow("Qualifier", dep.qualifier)
        }
        detailRow("Scope", dep.scope ?: "root")
        if (dep.secondaryTypes.isNotEmpty()) {
          detailRow("Secondary Types", dep.secondaryTypes.joinToString(", "))
        }
        detailRow("Has Callbacks", if (dep.hasCallbacks) "Yes" else "No")
      }
    }
    if (config.githubRepoUrl != null) {
      div {
        classes = setOf("mt-3")
        a(href = "${config.githubRepoUrl}/search?q=class+${dep.simpleName}+extension%3Akt&type=code") {
          attributes["target"] = "_blank"
          attributes["rel"] = "noopener noreferrer"
          classes = setOf(
            "inline-flex",
            "items-center",
            "gap-1.5",
            "text-sm",
            "text-indigo-600",
            "hover:text-indigo-800",
            "font-medium",
          )
          +"View on GitHub"
          unsafe { +externalLinkSmallIcon }
        }
      }
    }
  }
}

private fun TBODY.detailRow(label: String, value: String) {
  tr {
    td {
      classes = setOf("py-1", "pr-4", "text-gray-500", "font-medium", "whitespace-nowrap")
      +label
    }
    td {
      classes = setOf("py-1", "font-mono", "text-xs", "text-gray-900", "break-all")
      +value
    }
  }
}

private fun kindBadgeColor(kind: String): String =
  when (kind) {
    "Singleton" -> "bg-indigo-50 text-indigo-700"
    "Factory" -> "bg-green-50 text-green-700"
    "Scoped" -> "bg-yellow-50 text-yellow-700"
    else -> "bg-gray-100 text-gray-800"
  }

@Suppress("MaximumLineLength")
private val chevronRightIcon: String =
  """<svg class="h-3.5 w-3.5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5"/></svg>"""

@Suppress("MaximumLineLength")
private val externalLinkSmallIcon: String =
  """<svg class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M13.5 6H5.25A2.25 2.25 0 003 8.25v10.5A2.25 2.25 0 005.25 21h10.5A2.25 2.25 0 0018 18.75V10.5m-10.5 6L21 3m0 0h-5.25M21 3v5.25"/></svg>"""
