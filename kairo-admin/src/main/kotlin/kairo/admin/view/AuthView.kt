package kairo.admin.view

import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

@Suppress("LongMethod")
internal fun FlowContent.authView(modules: List<String>) {
  pageHeader(
    "Auth",
    "Stytch authentication provider. Shows available auth methods and API modules.",
  )
  div {
    classes = setOf("flex", "items-center", "gap-3", "mb-6")
    span {
      classes = setOf("px-2", "py-1", "text-xs", "bg-green-50", "text-green-700", "rounded-full", "font-medium")
      +"Connected"
    }
    span {
      classes = setOf("text-sm", "text-gray-500")
      +"Stytch"
    }
  }
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-6")
    table {
      classes = setOf("w-full", "text-sm")
      thead {
        tr {
          classes = setOf("border-b")
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"#"
          }
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Module"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Status"
          }
        }
      }
      tbody {
        modules.forEachIndexed { index, name ->
          tr {
            classes = setOf("border-b", "border-gray-100")
            td {
              classes = setOf("py-2", "pr-4", "text-gray-400", "font-mono", "text-xs")
              +"${index + 1}"
            }
            td {
              classes = setOf("py-2", "pr-4", "font-medium")
              +name
            }
            td {
              classes = setOf("py-2")
              span {
                classes = setOf("px-2", "py-1", "text-xs", "bg-green-50", "text-green-700", "rounded-full")
                +"Available"
              }
            }
          }
        }
      }
    }
  }
}
