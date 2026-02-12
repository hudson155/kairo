package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.EndpointInfo
import kairo.admin.model.ParamInfo
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.TBODY
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.select
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.textArea
import kotlinx.html.tr
import kotlinx.html.unsafe

@Suppress("LongMethod", "CognitiveComplexMethod")
internal fun FlowContent.endpointsView(
  config: AdminDashboardConfig,
  endpoints: List<EndpointInfo>,
  selectedIndex: Int? = null,
) {
  pageHeader(
    "Endpoints",
    "Browse and test registered REST API endpoints." +
      " Select an endpoint to view its metadata, fill in parameters, and send requests directly from the browser.",
  )
  if (endpoints.isEmpty()) {
    p {
      classes = setOf("text-gray-500")
      +"No endpoints registered."
    }
    return
  }
  // Endpoint selector dropdown.
  div {
    classes = setOf("mb-6")
    label {
      classes = setOf("block", "text-sm", "font-medium", "text-gray-700", "mb-1")
      +"Endpoint"
    }
    select {
      classes = setOf(
        "w-full",
        "p-2",
        "border",
        "border-gray-300",
        "shadow-sm",
        "rounded-md",
        "font-mono",
        "text-sm",
      )
      attributes["onchange"] = "if(this.value) window.location.href=this.value"
      option {
        value = ""
        selected = selectedIndex == null
        +"Select an endpoint..."
      }
      endpoints.forEachIndexed { index, endpoint ->
        option {
          value = "${config.pathPrefix}/endpoints/$index"
          selected = index == selectedIndex
          +"${endpoint.method} ${endpoint.path}"
        }
      }
    }
  }
  // Endpoint detail form.
  val endpoint = selectedIndex?.let { endpoints.getOrNull(it) }
  if (endpoint != null) {
    endpointForm(endpoint)
  }
}

@Suppress("LongMethod", "CognitiveComplexMethod")
private fun FlowContent.endpointForm(endpoint: EndpointInfo) {
  div {
    attributes["data-controller"] = "request"
    attributes["data-request-method-value"] = endpoint.method
    attributes["data-request-path-value"] = endpoint.path
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-6", "space-y-6")

    // Method + path heading with collapsible Details.
    div {
      attributes["data-controller"] = "toggle"
      div {
        classes = setOf("flex", "items-center", "justify-between")
        h3 {
          classes = setOf("text-lg", "font-semibold", "text-gray-900", "flex", "items-center", "gap-3")
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
            "cursor-pointer",
          )
          attributes["data-action"] = "toggle#toggle"
          +"Details"
          span {
            attributes["data-toggle-target"] = "icon"
            attributes["style"] = "transition: transform 150ms"
            unsafe { +chevronIcon }
          }
        }
      }
      // Collapsible endpoint details.
      endpointMetadataContent(endpoint)
    }

    if (endpoint.pathParams.isNotEmpty()) {
      h3 {
        classes = setOf("text-base", "font-semibold", "text-gray-900")
        +"Path Parameters"
      }
      div {
        classes = setOf("space-y-3")
        endpoint.pathParams.forEach { param ->
          div {
            label {
              classes = setOf("block", "text-sm", "font-medium", "leading-6", "text-gray-900", "mb-1")
              +"${param.name} (${param.type})"
            }
            input(type = InputType.text) {
              classes = setOf(
                "w-full",
                "p-2",
                "border",
                "border-gray-300",
                "shadow-sm",
                "rounded-md",
                "font-mono",
                "text-sm",
              )
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
        classes = setOf("text-base", "font-semibold", "text-gray-900")
        +"Query Parameters"
      }
      div {
        classes = setOf("space-y-3")
        endpoint.queryParams.forEach { param ->
          div {
            label {
              classes = setOf("block", "text-sm", "font-medium", "leading-6", "text-gray-900", "mb-1")
              +"${param.name} (${param.type})${if (param.required) " *" else ""}"
            }
            input(type = InputType.text) {
              classes = setOf(
                "w-full",
                "p-2",
                "border",
                "border-gray-300",
                "shadow-sm",
                "rounded-md",
                "font-mono",
                "text-sm",
              )
              attributes["data-request-target"] = "queryParam"
              attributes["data-param-name"] = param.name
              placeholder = param.name
            }
          }
        }
      }
    }

    // Custom headers section.
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
        classes = setOf("text-base", "font-semibold", "text-gray-900")
        +"Request Body (${endpoint.requestBodyType})"
      }
      div {
        attributes["data-controller"] = "json-editor"
        attributes["data-json-editor-schema-value"] =
          endpoint.requestBodyFields.joinToString(",", "[", "]") { field ->
            """{"name":"${field.name}","type":"${field.type}","required":${field.required}}"""
          }
        div {
          attributes["style"] = "position: relative;"
          pre {
            classes = setOf("font-mono", "text-sm", "rounded-md")
            attributes["data-json-editor-target"] = "highlight"
            attributes["style"] = "position: absolute; top: 0; left: 0; right: 0; bottom: 0;" +
              " padding: 0.75rem; overflow: hidden; pointer-events: none; background: white;" +
              " white-space: pre-wrap; word-wrap: break-word; border: 1px solid transparent;" +
              " margin: 0; line-height: 1.5; border-radius: 0.375rem;"
          }
          textArea {
            classes = setOf(
              "w-full",
              "p-3",
              "border",
              "border-gray-300",
              "shadow-sm",
              "rounded-md",
              "font-mono",
              "text-sm",
            )
            attributes["data-request-target"] = "requestBody"
            attributes["data-json-editor-target"] = "textarea"
            attributes["data-action"] = "input->json-editor#onInput scroll->json-editor#onScroll paste->json-editor#onPaste"
            attributes["style"] =
              "color: transparent; caret-color: #1f2937; background: transparent;" +
                " resize: none; position: relative; z-index: 1; line-height: 1.5;"
            +(endpoint.requestBodyExample ?: """{"key": "value"}""")
          }
        }
        div {
          classes = setOf("hidden", "mt-2", "bg-red-50", "rounded-lg", "p-3")
          attributes["data-json-editor-target"] = "error"
          attributes["style"] = "border-left: 4px solid #f87171; display: none;"
          div {
            classes = setOf("text-sm", "text-red-800")
            attributes["data-json-editor-target"] = "errorText"
          }
        }
      }
    }

    div {
      classes = setOf("flex", "justify-end")
      button(type = ButtonType.button) {
        classes = setOf(
          "px-6",
          "py-2",
          "bg-indigo-600",
          "text-white",
          "rounded-md",
          "hover:bg-indigo-500",
          "font-semibold",
          "shadow-sm",
        )
        attributes["data-action"] = "request#send"
        +"Send Request"
      }
    }

    // Response area.
    div {
      attributes["data-request-target"] = "responseArea"
      classes = setOf("hidden", "mt-6")
      h3 {
        classes = setOf("text-base", "font-semibold", "text-gray-900", "mb-2")
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
        classes = setOf("bg-gray-100", "text-gray-900", "p-4", "rounded-lg", "overflow-auto", "text-sm", "font-mono")
        id = "response-body"
        attributes["data-request-target"] = "responseBody"
      }
    }
  }
}

@Suppress("LongMethod", "CognitiveComplexMethod")
private fun FlowContent.endpointMetadataContent(endpoint: EndpointInfo) {
  div {
    classes = setOf("hidden", "mt-3")
    attributes["data-toggle-target"] = "content"
    div {
      classes = setOf("bg-gray-50", "rounded-lg", "p-4", "space-y-4")
      table {
        classes = setOf("w-full", "text-sm")
        tbody {
          metadataRow("Class", endpoint.qualifiedClassName)
          metadataRow("Kind", if (endpoint.isDataObject) "data object" else "data class")
          metadataRow("Content-Type", endpoint.contentType ?: "none")
          metadataRow("Accept", endpoint.accept ?: "none")
          metadataRow("Input Type", endpoint.inputType)
          metadataRow("Response Type", endpoint.responseType)
          if (endpoint.pathParams.isNotEmpty()) {
            metadataRow(
              "Path Params",
              endpoint.pathParams.joinToString(", ") { "${it.name}: ${it.type}" },
            )
          }
          if (endpoint.queryParams.isNotEmpty()) {
            metadataRow(
              "Query Params",
              endpoint.queryParams.joinToString(", ") {
                "${it.name}: ${it.type}${if (!it.required) "?" else ""}"
              },
            )
          }
        }
      }
      if (endpoint.requestBodyFields.isNotEmpty()) {
        collapsibleFieldsSection(
          "Request Body Fields (${endpoint.requestBodyType})",
          endpoint.requestBodyFields,
        )
      }
      if (endpoint.responseFields.isNotEmpty()) {
        collapsibleFieldsSection(
          "Response Fields (${endpoint.responseType})",
          endpoint.responseFields,
        )
      }
    }
  }
}

private fun FlowContent.collapsibleFieldsSection(title: String, fields: List<ParamInfo>) {
  div {
    attributes["data-controller"] = "toggle"
    button(type = ButtonType.button) {
      classes = setOf(
        "mt-2",
        "flex",
        "items-center",
        "gap-2",
        "text-base",
        "font-semibold",
        "text-gray-900",
        "cursor-pointer",
      )
      attributes["data-action"] = "toggle#toggle"
      span {
        attributes["data-toggle-target"] = "icon"
        attributes["style"] = "transition: transform 150ms"
        unsafe { +chevronIcon }
      }
      +title
    }
    div {
      classes = setOf("hidden", "mt-3")
      attributes["data-toggle-target"] = "content"
      fieldsTable(fields)
    }
  }
}

private fun FlowContent.fieldsTable(fields: List<ParamInfo>) {
  div {
    classes = setOf("bg-gray-50", "rounded-lg", "overflow-hidden")
    table {
      classes = setOf("w-full", "text-sm")
      tbody {
        fields.forEach { field ->
          tr {
            classes = setOf("border-b", "border-gray-100")
            td {
              classes = setOf("py-1", "px-4", "font-mono", "text-xs", "text-gray-900")
              +field.name
            }
            td {
              classes = setOf("py-1", "px-4", "font-mono", "text-xs", "text-gray-500")
              +field.type
            }
            td {
              classes = setOf("py-1", "px-4", "text-xs")
              if (field.required) {
                span {
                  classes = setOf("text-red-700")
                  +"required"
                }
              } else {
                span {
                  classes = setOf("text-gray-400")
                  +"optional"
                }
              }
            }
          }
        }
      }
    }
  }
}

private fun TBODY.metadataRow(label: String, value: String) {
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

private fun methodBadgeColor(method: String): String =
  when (method.uppercase()) {
    "GET" -> "bg-green-50 text-green-700"
    "POST" -> "bg-indigo-50 text-indigo-700"
    "PATCH", "PUT" -> "bg-yellow-50 text-yellow-700"
    "DELETE" -> "bg-red-50 text-red-700"
    else -> "bg-gray-100 text-gray-800"
  }

@Suppress("MaximumLineLength")
private val chevronIcon: String =
  """<svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5"/></svg>"""
