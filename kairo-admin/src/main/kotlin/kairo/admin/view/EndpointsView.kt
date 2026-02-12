package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.EndpointInfo
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.span

@Suppress("LongMethod")
internal fun FlowContent.endpointsView(config: AdminDashboardConfig, endpoints: List<EndpointInfo>) {
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6")
    +"Endpoints"
  }
  if (endpoints.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No endpoints registered."
    }
    return
  }
  div {
    classes = setOf("space-y-2")
    endpoints.forEachIndexed { index, endpoint ->
      a(href = "${config.pathPrefix}/endpoints/$index") {
        classes = setOf(
          "block",
          "bg-white",
          "rounded-lg",
          "shadow-sm",
          "hover:shadow-md",
          "transition",
          "p-4",
          "no-underline",
        )
        div {
          classes = setOf("flex", "items-center", "gap-3")
          span {
            classes = setOf(
              "px-3",
              "py-1",
              "rounded-md",
              "text-xs",
              "font-bold",
              "uppercase",
              methodColor(endpoint.method),
            )
            +endpoint.method
          }
          span {
            classes = setOf("font-mono", "text-sm", "text-gray-800")
            +endpoint.path
          }
          span {
            classes = setOf("ml-auto", "text-xs", "text-gray-400")
            +endpoint.endpointClassName
          }
        }
      }
    }
  }
}

private fun methodColor(method: String): String =
  when (method.uppercase()) {
    "GET" -> "bg-green-100 text-green-800"
    "POST" -> "bg-blue-100 text-blue-800"
    "PATCH", "PUT" -> "bg-yellow-100 text-yellow-800"
    "DELETE" -> "bg-red-100 text-red-800"
    else -> "bg-gray-100 text-gray-800"
  }
