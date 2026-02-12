package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.EndpointInfo
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.textArea

@Suppress("LongMethod", "CognitiveComplexMethod", "UnusedParameter")
internal fun FlowContent.endpointDetailView(
  config: AdminDashboardConfig,
  endpoint: EndpointInfo,
  index: Int,
) {
  div {
    classes = setOf("mb-4")
    a(href = "${config.pathPrefix}/endpoints") {
      classes = setOf("text-blue-600", "hover:text-blue-800", "text-sm")
      +"Back to endpoints"
    }
  }
  h1 {
    classes = setOf("text-2xl", "font-bold", "text-gray-900", "mb-6", "flex", "items-center", "gap-3")
    span {
      classes = setOf(
        "px-3",
        "py-1",
        "rounded-md",
        "text-sm",
        "font-bold",
        "uppercase",
        methodBadgeColor(endpoint.method),
      )
      +endpoint.method
    }
    +endpoint.path
  }
  div {
    attributes["data-controller"] = "request"
    attributes["data-request-method-value"] = endpoint.method
    attributes["data-request-path-value"] = endpoint.path
    classes = setOf("bg-white", "rounded-xl", "shadow-md", "p-6", "space-y-6")

    if (endpoint.pathParams.isNotEmpty()) {
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900")
        +"Path Parameters"
      }
      div {
        classes = setOf("space-y-3")
        endpoint.pathParams.forEach { param ->
          div {
            label {
              classes = setOf("block", "text-sm", "font-medium", "text-gray-700", "mb-1")
              +"${param.name} (${param.type})"
            }
            input(type = InputType.text) {
              classes = setOf("w-full", "p-2", "border", "border-gray-300", "rounded-md", "font-mono", "text-sm")
              attributes["data-request-target"] = "pathParam"
              attributes["data-param-name"] = param.name
              placeholder = param.name
            }
          }
        }
      }
    }

    if (endpoint.queryParams.isNotEmpty()) {
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900")
        +"Query Parameters"
      }
      div {
        classes = setOf("space-y-3")
        endpoint.queryParams.forEach { param ->
          div {
            label {
              classes = setOf("block", "text-sm", "font-medium", "text-gray-700", "mb-1")
              +"${param.name} (${param.type})${if (param.required) " *" else ""}"
            }
            input(type = InputType.text) {
              classes = setOf("w-full", "p-2", "border", "border-gray-300", "rounded-md", "font-mono", "text-sm")
              attributes["data-request-target"] = "queryParam"
              attributes["data-param-name"] = param.name
              placeholder = param.name
            }
          }
        }
      }
    }

    // Custom headers section.
    h3 {
      classes = setOf("text-lg", "font-semibold", "text-gray-900")
      +"Custom Headers"
    }
    div {
      attributes["data-controller"] = "headers"
      div {
        attributes["data-headers-target"] = "container"
        classes = setOf("space-y-2")
      }
      button(type = ButtonType.button) {
        classes = setOf(
          "mt-2",
          "px-3",
          "py-1",
          "text-sm",
          "bg-gray-100",
          "text-gray-700",
          "rounded-md",
          "hover:bg-gray-200",
        )
        attributes["data-action"] = "headers#add"
        +"+ Add Header"
      }
    }

    if (endpoint.requestBodyType != null) {
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900")
        +"Request Body (${endpoint.requestBodyType})"
      }
      textArea {
        classes = setOf("w-full", "h-32", "p-3", "border", "border-gray-300", "rounded-md", "font-mono", "text-sm")
        attributes["data-request-target"] = "requestBody"
        placeholder = """{"key": "value"}"""
      }
    }

    div {
      classes = setOf("flex", "items-center", "gap-4")
      button(type = ButtonType.button) {
        classes = setOf("px-6", "py-2", "bg-blue-600", "text-white", "rounded-md", "hover:bg-blue-700", "font-medium")
        attributes["data-action"] = "request#send"
        +"Send Request"
      }
      span {
        classes = setOf("text-sm", "text-gray-500")
        +"Response: ${endpoint.responseType}"
      }
    }

    // Response area.
    div {
      attributes["data-request-target"] = "responseArea"
      classes = setOf("hidden", "mt-6")
      h3 {
        classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-2")
        +"Response"
      }
      div {
        classes = setOf("flex", "items-center", "gap-3", "mb-2")
        span {
          classes = setOf("text-sm", "font-medium")
          attributes["data-request-target"] = "statusCode"
        }
        span {
          classes = setOf("text-xs", "text-gray-400")
          attributes["data-request-target"] = "responseTime"
        }
      }
      pre {
        classes = setOf("bg-gray-900", "text-green-400", "p-4", "rounded-lg", "overflow-auto", "text-sm", "font-mono")
        id = "response-body"
        attributes["data-request-target"] = "responseBody"
      }
    }
  }
}

private fun methodBadgeColor(method: String): String =
  when (method.uppercase()) {
    "GET" -> "bg-green-100 text-green-800"
    "POST" -> "bg-blue-100 text-blue-800"
    "PATCH", "PUT" -> "bg-yellow-100 text-yellow-800"
    "DELETE" -> "bg-red-100 text-red-800"
    else -> "bg-gray-100 text-gray-800"
  }
