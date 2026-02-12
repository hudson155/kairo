package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kairo.admin.model.DashboardStats
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.unsafe

internal fun FlowContent.homeView(config: AdminDashboardConfig, stats: DashboardStats) {
  h1 {
    classes = setOf("text-3xl", "font-bold", "text-gray-900", "mb-8")
    +"Welcome to ${config.title}"
  }
  div {
    classes = setOf("grid", "grid-cols-1", "md:grid-cols-2", "gap-6", "max-w-4xl", "mb-8")
    dashboardCard(
      href = "${config.pathPrefix}/endpoints",
      icon = endpointsIcon,
      title = "Endpoints",
      description = "Explore and test all ${stats.endpointCount} registered API endpoints",
    )
    dashboardCard(
      href = "${config.pathPrefix}/config",
      icon = configIcon,
      title = "Config",
      description = "View ${stats.configFileCount} HOCON configuration files",
    )
    dashboardCard(
      href = "${config.pathPrefix}/jvm",
      icon = jvmIcon,
      title = "JVM",
      description = "Monitor memory, threads, and garbage collection",
    )
    dashboardCard(
      href = "${config.pathPrefix}/database",
      icon = databaseIcon,
      title = "Database",
      description = "Browse tables and run read-only SQL queries",
    )
  }
  div {
    classes = setOf("flex", "flex-wrap", "gap-3", "max-w-4xl")
    statBadge("${stats.endpointCount} endpoints")
    statBadge("${stats.configFileCount} config files")
    statBadge("${stats.tableCount} tables")
    statBadge("Uptime: ${stats.jvmUptime}")
  }
}

private fun FlowContent.dashboardCard(
  href: String,
  icon: String,
  title: String,
  description: String,
) {
  a(href = href) {
    classes = setOf(
      "bg-white",
      "rounded-xl",
      "shadow-md",
      "hover:shadow-lg",
      "transition",
      "p-6",
      "cursor-pointer",
      "block",
      "no-underline",
    )
    div {
      classes = setOf("mb-4", "text-blue-600")
      unsafe { +icon }
    }
    h3 {
      classes = setOf("text-lg", "font-semibold", "text-gray-900", "mb-2")
      +title
    }
    p {
      classes = setOf("text-gray-600", "text-sm")
      +description
    }
  }
}

private fun FlowContent.statBadge(text: String) {
  span {
    classes = setOf("px-3", "py-1", "bg-gray-200", "text-gray-700", "rounded-full", "text-sm")
    +text
  }
}

@Suppress("MaximumLineLength")
private val endpointsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m9.86-2.554a4.5 4.5 0 00-6.364-6.364L4.5 8.25l4.5 4.5"/></svg>"""

@Suppress("MaximumLineLength")
private val configIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93s.844.141 1.193-.106l.738-.527a1.125 1.125 0 011.45.12l.773.774c.39.389.44 1.002.12 1.45l-.527.737c-.247.35-.272.806-.106 1.193s.506.71.93.78l.893.15c.543.09.94.56.94 1.109v1.094c0 .55-.397 1.02-.94 1.11l-.893.149c-.424.07-.764.383-.93.78s-.141.843.106 1.193l.527.738c.32.447.269 1.06-.12 1.45l-.774.773a1.125 1.125 0 01-1.449.12l-.738-.527c-.35-.247-.806-.272-1.193-.106s-.71.506-.78.93l-.15.894c-.09.542-.56.94-1.109.94h-1.094c-.55 0-1.02-.398-1.11-.94l-.148-.894c-.071-.424-.384-.764-.781-.93s-.843-.141-1.193.106l-.738.527a1.125 1.125 0 01-1.45-.12l-.773-.774a1.125 1.125 0 01-.12-1.45l.527-.737c.247-.35.272-.806.106-1.193s-.506-.71-.93-.78l-.894-.15c-.542-.09-.94-.56-.94-1.109v-1.094c0-.55.398-1.02.94-1.11l.894-.149c.424-.07.764-.383.93-.78s.141-.843-.106-1.193l-.527-.738a1.125 1.125 0 01.12-1.45l.773-.773a1.125 1.125 0 011.45-.12l.737.527c.35.247.807.272 1.194.107s.71-.507.78-.931l.149-.894zM15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>"""

@Suppress("MaximumLineLength")
private val jvmIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.25 3v1.5M4.5 8.25H3m18 0h-1.5M4.5 12H3m18 0h-1.5m-15 3.75H3m18 0h-1.5M8.25 19.5V21M12 3v1.5m0 15V21m3.75-18v1.5m0 15V21m-9-1.5h10.5a2.25 2.25 0 002.25-2.25V6.75a2.25 2.25 0 00-2.25-2.25H6.75A2.25 2.25 0 004.5 6.75v10.5a2.25 2.25 0 002.25 2.25z"/></svg>"""

@Suppress("MaximumLineLength")
private val databaseIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M20.25 6.375c0 2.278-3.694 4.125-8.25 4.125S3.75 8.653 3.75 6.375m16.5 0c0-2.278-3.694-4.125-8.25-4.125S3.75 4.097 3.75 6.375m16.5 0v11.25c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125V6.375m16.5 0v3.75m-16.5-3.75v3.75m16.5 0v3.75C20.25 16.153 16.556 18 12 18s-8.25-1.847-8.25-4.125v-3.75m16.5 0c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125"/></svg>"""
