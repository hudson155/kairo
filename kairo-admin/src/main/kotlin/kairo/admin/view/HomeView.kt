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

@Suppress("LongMethod")
internal fun FlowContent.homeView(config: AdminDashboardConfig, stats: DashboardStats) {
  h1 {
    classes = setOf("text-2xl", "font-semibold", "text-gray-900", "mb-6")
    +"Welcome to the ${config.title} admin dashboard!"
  }
  div {
    classes = setOf("flex", "flex-wrap", "gap-3", "max-w-4xl", "mb-6")
    statBadge("${stats.endpointCount} endpoints")
    statBadge("${stats.featureCount} features")
    statBadge("${stats.tableCount} tables")
    statBadge("Uptime: ${stats.jvmUptime}")
  }
  div {
    classes = setOf("grid", "grid-cols-1", "md:grid-cols-2", "lg:grid-cols-3", "gap-4", "max-w-4xl", "mb-8")
    dashboardCard(
      href = "${config.pathPrefix}/config",
      icon = configIcon,
      title = "Config",
      description = "View ${stats.configFileCount} HOCON configuration files",
    )
    dashboardCard(
      href = "${config.pathPrefix}/database",
      icon = databaseIcon,
      title = "Database",
      description = "Browse ${stats.tableCount} tables and run read-only SQL queries",
    )
    dashboardCard(
      href = "${config.pathPrefix}/dependencies",
      icon = dependenciesIcon,
      title = "Dependencies",
      description = "Inspect ${stats.dependencyCount} registered DI bindings",
    )
    if (config.docsUrl != null) {
      dashboardCard(
        href = config.docsUrl,
        icon = docsIcon,
        title = "Docs",
        description = "README, guides, and project documentation",
        external = true,
      )
    }
    dashboardCard(
      href = "${config.pathPrefix}/endpoints",
      icon = endpointsIcon,
      title = "Endpoints",
      description = "Explore and test all ${stats.endpointCount} registered API endpoints",
    )
    dashboardCard(
      href = "${config.pathPrefix}/errors",
      icon = errorsIcon,
      title = "Errors",
      description = "${stats.errorCount} errors recorded",
    )
    dashboardCard(
      href = "${config.pathPrefix}/features",
      icon = featuresIcon,
      title = "Features",
      description = "View ${stats.featureCount} registered application features",
    )
    dashboardCard(
      href = "${config.pathPrefix}/health",
      icon = healthIcon,
      title = "Health",
      description = "Run ${stats.healthCheckCount} health checks and monitor status",
    )
    dashboardCard(
      href = "${config.pathPrefix}/integrations",
      icon = integrationsIcon,
      title = "Integrations",
      description = "Monitor ${stats.integrationCount} external service connections",
    )
    dashboardCard(
      href = "${config.pathPrefix}/jvm",
      icon = jvmIcon,
      title = "JVM",
      description = "Monitor memory, threads, and garbage collection",
    )
    if (config.apiDocsUrl != null) {
      dashboardCard(
        href = config.apiDocsUrl,
        icon = apiDocsIcon,
        title = "Kairo Docs",
        description = "Kairo framework documentation and guides",
        external = true,
      )
    }
    dashboardCard(
      href = "${config.pathPrefix}/logging",
      icon = loggingIcon,
      title = "Logging",
      description = "View and change logger levels at runtime",
    )
  }
}

@Suppress("LongParameterList")
private fun FlowContent.dashboardCard(
  href: String,
  icon: String,
  title: String,
  description: String,
  external: Boolean = false,
) {
  a(href = href) {
    if (external) {
      attributes["target"] = "_blank"
      attributes["rel"] = "noopener noreferrer"
    }
    classes = setOf(
      "bg-white",
      "rounded-lg",
      "shadow-sm",
      "hover:shadow-md",
      "transition",
      "p-5",
      "cursor-pointer",
      "block",
      "no-underline",
      "border",
      "border-gray-200",
    )
    div {
      classes = setOf("mb-3", "text-indigo-600")
      unsafe { +icon }
    }
    h3 {
      classes = setOf("text-base", "font-semibold", "leading-6", "text-gray-900", "mb-1")
      +title
    }
    p {
      classes = setOf("text-gray-500", "text-sm")
      +description
    }
  }
}

private fun FlowContent.statBadge(text: String) {
  span {
    classes = setOf("px-3", "py-1", "bg-gray-100", "text-gray-600", "rounded-md", "text-sm", "font-medium")
    +text
  }
}

@Suppress("MaximumLineLength")
private val endpointsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m9.86-2.554a4.5 4.5 0 00-6.364-6.364L4.5 8.25l4.5 4.5"/></svg>"""

@Suppress("MaximumLineLength")
private val configIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93s.844.141 1.193-.106l.738-.527a1.125 1.125 0 011.45.12l.773.774c.39.389.44 1.002.12 1.45l-.527.737c-.247.35-.272.806-.106 1.193s.506.71.93.78l.893.15c.543.09.94.56.94 1.109v1.094c0 .55-.397 1.02-.94 1.11l-.893.149c-.424.07-.764.383-.93.78s-.141.843.106 1.193l.527.738c.32.447.269 1.06-.12 1.45l-.774.773a1.125 1.125 0 01-1.449.12l-.738-.527c-.35-.247-.806-.272-1.193-.106s-.71.506-.78.93l-.15.894c-.09.542-.56.94-1.109.94h-1.094c-.55 0-1.02-.398-1.11-.94l-.148-.894c-.071-.424-.384-.764-.781-.93s-.843-.141-1.193.106l-.738.527a1.125 1.125 0 01-1.45-.12l-.773-.774a1.125 1.125 0 01-.12-1.45l.527-.737c.247-.35.272-.806.106-1.193s-.506-.71-.93-.78l-.894-.15c-.542-.09-.94-.56-.94-1.109v-1.094c0-.55.398-1.02.94-1.11l.894-.149c.424-.07.764-.383.93-.78s.141-.843-.106-1.193l-.527-.738a1.125 1.125 0 01.12-1.45l.773-.773a1.125 1.125 0 011.45-.12l.737.527c.35.247.807.272 1.194.107s.71-.507.78-.931l.149-.894zM15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>"""

@Suppress("MaximumLineLength")
private val jvmIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.25 3v1.5M4.5 8.25H3m18 0h-1.5M4.5 12H3m18 0h-1.5m-15 3.75H3m18 0h-1.5M8.25 19.5V21M12 3v1.5m0 15V21m3.75-18v1.5m0 15V21m-9-1.5h10.5a2.25 2.25 0 002.25-2.25V6.75a2.25 2.25 0 00-2.25-2.25H6.75A2.25 2.25 0 004.5 6.75v10.5a2.25 2.25 0 002.25 2.25z"/></svg>"""

@Suppress("MaximumLineLength")
private val databaseIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M20.25 6.375c0 2.278-3.694 4.125-8.25 4.125S3.75 8.653 3.75 6.375m16.5 0c0-2.278-3.694-4.125-8.25-4.125S3.75 4.097 3.75 6.375m16.5 0v11.25c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125V6.375m16.5 0v3.75m-16.5-3.75v3.75m16.5 0v3.75C20.25 16.153 16.556 18 12 18s-8.25-1.847-8.25-4.125v-3.75m16.5 0c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125"/></svg>"""

@Suppress("MaximumLineLength")
private val healthIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75m-3-7.036A11.959 11.959 0 013.598 6 11.99 11.99 0 003 9.749c0 5.592 3.824 10.29 9 11.623 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.571-.598-3.751h-.152c-3.196 0-6.1-1.248-8.25-3.285z"/></svg>"""

@Suppress("MaximumLineLength")
private val featuresIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M6.429 9.75L2.25 12l4.179 2.25m0-4.5l5.571 3 5.571-3m-11.142 0L2.25 7.5 12 2.25l9.75 5.25-4.179 2.25m0 0L12 12.75 6.429 9.75m11.142 0l4.179 2.25-9.75 5.25-9.75-5.25 4.179-2.25"/></svg>"""

@Suppress("MaximumLineLength")
private val dependenciesIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M21 7.5l-9-5.25L3 7.5m18 0l-9 5.25m9-5.25v9l-9 5.25M3 7.5l9 5.25M3 7.5v9l9 5.25m0-9v9"/></svg>"""

@Suppress("MaximumLineLength")
private val integrationsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M13.5 16.875h3.375m0 0h3.375m-3.375 0V13.5m0 3.375v3.375M6 10.5h2.25a2.25 2.25 0 002.25-2.25V6a2.25 2.25 0 00-2.25-2.25H6A2.25 2.25 0 003.75 6v2.25A2.25 2.25 0 006 10.5zm0 9.75h2.25A2.25 2.25 0 0010.5 18v-2.25a2.25 2.25 0 00-2.25-2.25H6a2.25 2.25 0 00-2.25 2.25V18A2.25 2.25 0 006 20.25zm9.75-9.75H18a2.25 2.25 0 002.25-2.25V6A2.25 2.25 0 0018 3.75h-2.25A2.25 2.25 0 0013.5 6v2.25a2.25 2.25 0 002.25 2.25z"/></svg>"""

@Suppress("MaximumLineLength")
private val loggingIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"/></svg>"""

@Suppress("MaximumLineLength")
private val errorsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"/></svg>"""

@Suppress("MaximumLineLength")
private val docsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"/></svg>"""

@Suppress("MaximumLineLength")
private val apiDocsIcon: String = """<svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M17.25 6.75L22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3l-4.5 16.5"/></svg>"""
