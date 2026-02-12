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
internal fun FlowContent.slackView(channels: Map<String, String>) {
  pageHeader(
    "Slack",
    "Slack integration channel mappings. Shows configured channel names and their Slack channel IDs.",
  )
  div {
    classes = setOf("flex", "items-center", "gap-3", "mb-6")
    span {
      classes = setOf("px-2", "py-1", "text-xs", "bg-green-50", "text-green-700", "rounded-full", "font-medium")
      +"Connected"
    }
  }
  if (channels.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No channel mappings configured."
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
            +"Channel Name"
          }
          th {
            classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
            +"Channel ID"
          }
        }
      }
      tbody {
        channels.entries.sortedBy { it.key }.forEach { (name, id) ->
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
