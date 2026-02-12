package kairo.admin.view

import kairo.admin.model.AdminDependencyInfo
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

@Suppress("LongMethod")
internal fun FlowContent.dependenciesView(dependencies: List<AdminDependencyInfo>) {
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6")
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
    classes = setOf("bg-white", "rounded-xl", "shadow-md", "p-6")
    table {
      classes = setOf("w-full", "text-sm")
      thead {
        tr {
          classes = setOf("border-b")
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Class"
          }
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Kind"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Qualifier"
          }
        }
      }
      tbody {
        dependencies.forEach { dep ->
          tr {
            classes = setOf("border-b", "border-gray-100")
            td {
              classes = setOf("py-2", "pr-4", "font-mono", "text-xs", "break-all")
              +dep.className
            }
            td {
              classes = setOf("py-2", "pr-4")
              span {
                classes = setOf("px-2", "py-1", "text-xs", "bg-blue-100", "text-blue-800", "rounded-full")
                +dep.kind
              }
            }
            td {
              classes = setOf("py-2", "font-mono", "text-xs")
              +(dep.qualifier ?: "-")
            }
          }
        }
      }
    }
  }
}
