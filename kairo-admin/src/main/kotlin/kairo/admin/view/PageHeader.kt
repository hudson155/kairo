package kairo.admin.view

import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.unsafe

@Suppress("LongMethod")
internal fun FlowContent.pageHeader(title: String, description: String) {
  div {
    attributes["data-controller"] = "toggle"
    classes = setOf("mb-6")
    div {
      classes = setOf("flex", "items-start", "justify-between")
      h1 {
        classes = setOf("text-2xl", "font-semibold", "text-gray-900")
        +title
      }
      button(type = ButtonType.button) {
        classes = setOf(
          "flex",
          "items-center",
          "gap-1.5",
          "px-3",
          "py-1.5",
          "rounded-md",
          "text-sm",
          "font-medium",
          "text-gray-500",
          "hover:text-gray-700",
          "hover:bg-gray-100",
          "transition-colors",
          "flex-shrink-0",
          "ml-4",
        )
        attributes["data-action"] = "toggle#toggle"
        +"Documentation"
        span {
          attributes["data-toggle-target"] = "icon"
          attributes["style"] = "display: inline-flex; transition: transform 150ms"
          unsafe { +chevronDownIcon }
        }
      }
    }
    div {
      classes = setOf(
        "hidden",
        "mt-3",
        "w-full",
        "bg-white",
        "rounded-lg",
        "shadow-sm",
        "border",
        "border-gray-200",
        "p-4",
      )
      attributes["data-toggle-target"] = "content"
      p {
        classes = setOf("text-sm", "text-gray-600", "leading-relaxed")
        +description
      }
    }
  }
}

@Suppress("MaximumLineLength")
private val chevronDownIcon: String =
  """<svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5"/></svg>"""
