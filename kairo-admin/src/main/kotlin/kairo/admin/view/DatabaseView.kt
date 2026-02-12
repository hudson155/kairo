package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.ColumnInfo
import kairo.admin.model.PoolStats
import kairo.admin.model.SqlQueryResult
import kairo.admin.model.TableInfo
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h3
import kotlinx.html.hiddenInput
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.select
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.textArea
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import kotlinx.html.unsafe

@Suppress("MaximumLineLength")
private val chevronIcon: String =
  """<svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5"/></svg>"""

@Suppress("LongMethod", "CognitiveComplexMethod", "LongParameterList")
internal fun FlowContent.databaseView(
  config: AdminDashboardConfig,
  tables: List<TableInfo>,
  selectedTable: String?,
  columns: List<ColumnInfo>?,
  queryResult: SqlQueryResult? = null,
  querySql: String = "",
  poolStats: PoolStats? = null,
) {
  pageHeader(
    "Database",
    "Browse database tables, view column schemas, and run read-only SQL queries." +
      " Only SELECT, WITH, EXPLAIN, and SHOW statements are allowed." +
      " All queries run in a READ ONLY transaction. Shows connection pool statistics.",
  )
  if (poolStats != null) {
    poolStatsBar(poolStats)
  }
  // Table selector dropdown.
  if (tables.isNotEmpty()) {
    div {
      classes = setOf("mb-6")
      label {
        classes = setOf("block", "text-sm", "font-medium", "text-gray-700", "mb-1")
        +"Table"
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
          selected = selectedTable == null
          +"Select a table..."
        }
        tables.forEach { tbl ->
          val fullName = "${tbl.schema}.${tbl.name}"
          option {
            value = "${config.pathPrefix}/database/tables/${tbl.schema}/${tbl.name}"
            selected = fullName == selectedTable
            +fullName
          }
        }
      }
    }
  } else {
    p {
      classes = setOf("text-gray-500", "text-sm", "mb-6")
      +"No tables found."
    }
  }
  div {
    classes = setOf("space-y-6")
    // Table schema.
    if (selectedTable != null && columns != null) {
      tableSchemaCard(selectedTable, columns)
    }
    // SQL query area.
    sqlQueryArea(config, selectedTable, querySql)
    // Query results (inline).
    if (queryResult != null) {
      queryResultCard(queryResult)
    }
  }
}

@Suppress("LongMethod", "CognitiveComplexMethod")
private fun FlowContent.tableSchemaCard(selectedTable: String, columns: List<ColumnInfo>) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-6")
    attributes["data-controller"] = "toggle"
    button(type = ButtonType.button) {
      classes = setOf(
        "flex",
        "items-center",
        "gap-2",
        "text-lg",
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
      +"Schema: $selectedTable (${columns.size} columns)"
    }
    div {
      classes = setOf("hidden", "mt-3")
      attributes["data-toggle-target"] = "content"
      if (columns.isEmpty()) {
        p {
          classes = setOf("text-gray-500")
          +"No columns found."
        }
      } else {
        table {
          classes = setOf("w-full", "text-sm")
          thead {
            tr {
              classes = setOf("border-b")
              th {
                classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
                +"Column"
              }
              th {
                classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
                +"Type"
              }
              th {
                classes = setOf("text-left", "py-2", "pr-4", "font-medium", "text-gray-500")
                +"Nullable"
              }
              th {
                classes = setOf("text-left", "py-2", "font-medium", "text-gray-500")
                +"Default"
              }
            }
          }
          tbody {
            columns.forEach { col ->
              tr {
                classes = setOf("border-b", "border-gray-100")
                td {
                  classes = setOf("py-2", "pr-4", "font-mono")
                  +col.name
                }
                td {
                  classes = setOf("py-2", "pr-4")
                  +col.dataType
                }
                td {
                  classes = setOf("py-2", "pr-4")
                  +if (col.isNullable) "YES" else "NO"
                }
                td {
                  classes = setOf("py-2", "font-mono", "text-xs")
                  +(col.defaultValue ?: "-")
                }
              }
            }
          }
        }
      }
    }
  }
}

@Suppress("LongMethod")
private fun FlowContent.queryResultCard(result: SqlQueryResult) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-6")
    h3 {
      classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-3")
      +"Results"
    }
    if (result.error != null) {
      div {
        classes = setOf("bg-red-50", "border", "border-red-200", "text-red-700", "px-4", "py-3", "rounded-lg")
        +result.error
      }
    } else {
      div {
        classes = setOf("flex", "items-center", "gap-3", "mb-4")
        span {
          classes = setOf("text-sm", "text-gray-500")
          +"${result.rowCount} rows"
        }
        span {
          classes = setOf("text-sm", "text-gray-400")
          +"${result.executionTimeMs}ms"
        }
      }
      if (result.columns.isNotEmpty()) {
        div {
          classes = setOf("overflow-auto")
          table {
            classes = setOf("w-full", "text-sm")
            thead {
              tr {
                classes = setOf("border-b")
                result.columns.forEach { col ->
                  th {
                    classes = setOf(
                      "text-left",
                      "py-2",
                      "px-3",
                      "font-medium",
                      "text-gray-500",
                      "whitespace-nowrap",
                    )
                    +col
                  }
                }
              }
            }
            tbody {
              result.rows.forEach { row ->
                tr {
                  classes = setOf("border-b", "border-gray-100", "hover:bg-gray-50")
                  row.forEach { cell ->
                    td {
                      classes = setOf("py-2", "px-3", "font-mono", "text-xs", "whitespace-nowrap")
                      +(cell ?: "NULL")
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Suppress("LongMethod")
private fun FlowContent.sqlQueryArea(config: AdminDashboardConfig, selectedTable: String?, currentSql: String = "") {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-6")
    attributes["data-controller"] = "sql"
    h3 {
      classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-3")
      +"SQL Query"
    }
    // Preset buttons.
    if (selectedTable != null) {
      val parts = selectedTable.split(".")
      val schema = parts.getOrElse(0) { "public" }
      val tableName = parts.getOrElse(1) { selectedTable }
      div {
        classes = setOf("flex", "flex-wrap", "gap-2", "mb-3")
        presetButton("SELECT *", "SELECT * FROM $schema.$tableName;")
        presetButton("LIMIT 10", "SELECT * FROM $schema.$tableName LIMIT 10;")
        presetButton("COUNT", "SELECT COUNT(*) FROM $schema.$tableName;")
        presetButton(
          "COLUMNS",
          "SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_schema = '$schema' AND table_name = '$tableName' ORDER BY ordinal_position;",
        )
        presetButton("SAMPLE", "SELECT * FROM $schema.$tableName ORDER BY RANDOM() LIMIT 5;")
      }
    }
    form(action = "${config.pathPrefix}/database/query", method = FormMethod.post) {
      if (selectedTable != null) {
        hiddenInput(name = "table") { value = selectedTable }
      }
      textArea {
        name = "sql"
        classes = setOf(
          "w-full",
          "h-24",
          "p-3",
          "border",
          "border-gray-300",
          "shadow-sm",
          "rounded-md",
          "font-mono",
          "text-sm",
          "resize-y",
        )
        attributes["data-sql-target"] = "queryInput"
        placeholder = "SELECT * FROM ..."
        +currentSql
      }
      div {
        classes = setOf("mt-3")
        button(type = ButtonType.submit) {
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
          +"Run Query"
        }
      }
    }
  }
}

private fun FlowContent.poolStatsBar(stats: PoolStats) {
  div {
    classes = setOf("bg-white", "rounded-lg", "shadow-sm", "p-4", "mb-6", "flex", "flex-wrap", "gap-4", "items-center")
    span {
      classes = setOf("text-sm", "font-semibold", "text-gray-700")
      +"Connection Pool"
    }
    poolBadge("Active", "${stats.acquiredSize}")
    poolBadge("Idle", "${stats.idleSize}")
    poolBadge("Allocated", "${stats.allocatedSize}/${stats.maxAllocatedSize}")
    poolBadge("Pending", "${stats.pendingAcquireSize}")
  }
}

private fun FlowContent.poolBadge(label: String, value: String) {
  span {
    classes = setOf("text-xs", "text-gray-500")
    +"$label: "
  }
  span {
    classes = setOf("text-xs", "font-mono", "font-medium", "text-gray-900", "mr-3")
    +value
  }
}

private fun FlowContent.presetButton(label: String, query: String) {
  button(type = ButtonType.button) {
    classes = setOf(
      "px-3",
      "py-1",
      "text-xs",
      "bg-gray-100",
      "text-gray-700",
      "rounded-md",
      "hover:bg-gray-200",
      "font-mono",
    )
    attributes["data-action"] = "sql#preset"
    attributes["data-query"] = query
    +label
  }
}
