package kairo.admin.view

import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

@Suppress("LongMethod")
internal fun FlowContent.emailView(templates: Map<String, String>) {
  pageHeader(
    "Email",
    "MailerSend email integration. Shows configured email templates and their IDs.",
  )
  div {
    classes = setOf("flex", "items-center", "gap-3", "mb-6")
    span {
      classes = setOf("px-2", "py-1", "text-xs", "bg-green-50", "text-green-700", "rounded-full", "font-medium")
      +"Connected"
    }
    span {
      classes = setOf("text-sm", "text-gray-500")
      +"MailerSend"
    }
  }
  if (templates.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No email templates configured."
    }
    return
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
            +"Template Name"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Template ID"
          }
        }
      }
      tbody {
        templates.entries.sortedBy { it.key }.forEach { (name, id) ->
          tr {
            classes = setOf("border-b", "border-gray-100")
            td {
              classes = setOf("py-2", "pr-4", "font-medium")
              +name
            }
            td {
              classes = setOf("py-2", "font-mono", "text-xs", "text-gray-600")
              +id
            }
          }
        }
      }
    }
  }
}
