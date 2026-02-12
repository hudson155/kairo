package kairo.admin.view

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
internal fun FlowContent.featuresView(featureNames: List<String>) {
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6")
    +"Features"
  }
  if (featureNames.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No features registered."
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
            +"#"
          }
          th {
            classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
            +"Feature"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Status"
          }
        }
      }
      tbody {
        featureNames.forEachIndexed { index, name ->
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
                classes = setOf("px-2", "py-1", "text-xs", "bg-green-100", "text-green-800", "rounded-full")
                +"Running"
              }
            }
          }
        }
      }
    }
  }
}
