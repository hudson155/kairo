package kairo.admin.view

import kairo.admin.AdminDashboardConfig
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.hr
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.unsafe

@Suppress("LongMethod", "CognitiveComplexMethod", "LongParameterList")
internal fun HTML.adminLayout(
  config: AdminDashboardConfig,
  optionalTabs: Set<String> = emptySet(),
  activeTab: String,
  content: FlowContent.() -> Unit,
) {
  head {
    meta(charset = "utf-8")
    meta(name = "viewport", content = "width=device-width, initial-scale=1")
    title { +"${config.serverName ?: config.title} - Admin" }
    link(rel = "icon", type = "image/png", href = "${config.pathPrefix}/static/img/logo.png")
    link(rel = "stylesheet", href = "${config.pathPrefix}/static/css/tailwind.css")
    script(src = "${config.pathPrefix}/static/vendor/turbo.es2017-esm.js") {
      attributes["type"] = "module"
    }
    script(src = "${config.pathPrefix}/static/js/admin.js") {
      attributes["type"] = "module"
    }
  }
  body {
    classes = setOf("bg-white", "min-h-screen")
    attributes["data-controller"] = "sidebar"
    // Overlay for mobile sidebar.
    div {
      classes = setOf("fixed", "inset-0", "bg-gray-900", "z-40", "hidden")
      attributes["data-sidebar-target"] = "overlay"
      attributes["data-action"] = "click->sidebar#close"
      attributes["style"] = "opacity: 0.5"
    }
    // Hamburger button visible only on small screens.
    div {
      classes = setOf("md:hidden", "bg-gray-900", "p-3", "flex", "items-center")
      button {
        classes = setOf("text-gray-400", "hover:text-white", "p-2")
        attributes["data-action"] = "click->sidebar#toggle"
        // Hamburger icon (visible when closed).
        span {
          attributes["data-sidebar-target"] = "openIcon"
          unsafe {
            +hamburgerIcon
          }
        }
        // X icon (visible when open).
        span {
          classes = setOf("hidden")
          attributes["data-sidebar-target"] = "closeIcon"
          unsafe {
            +closeIcon
          }
        }
      }
      span {
        classes = setOf("text-white", "font-semibold", "ml-auto", "pr-4")
        +(config.serverName ?: config.title)
      }
    }
    div {
      classes = setOf("flex", "min-h-screen")
      // Sidebar: hidden off-screen on mobile, always visible on md+.
      nav {
        classes = setOf(
          "w-72",
          "bg-gray-900",
          "text-white",
          "min-h-screen",
          "flex",
          "flex-col",
          "gap-y-5",
          "p-6",
          "fixed",
          "inset-y-0",
          "left-0",
          "z-50",
          "-translate-x-full",
          "transition-transform",
          "md:relative",
          "md:translate-x-0",
          "md:flex-shrink-0",
        )
        attributes["data-sidebar-target"] = "sidebar"
        a(href = "${config.pathPrefix}/") {
          classes = setOf(
            "flex",
            "items-center",
            "gap-3",
            "mb-6",
            "hover:text-gray-300",
          )
          img(src = "${config.pathPrefix}/static/img/logo.png", alt = "Logo") {
            classes = setOf("h-8", "w-8", "rounded-lg")
          }
          span {
            classes = setOf("text-lg", "font-semibold", "tracking-tight")
            +(config.serverName ?: config.title)
          }
        }
        div {
          classes = setOf("space-y-1")
          tabLink("Home", "", activeTab, config)
          buildList {
            if ("auth" in optionalTabs) add("Auth" to "auth")
            add("Config" to "config")
            add("Database" to "database")
            add("Dependencies" to "dependencies")
            if ("email" in optionalTabs) add("Email" to "email")
            add("Endpoints" to "endpoints")
            add("Errors" to "errors")
            add("Features" to "features")
            add("Health" to "health")
            add("Integrations" to "integrations")
            add("JVM" to "jvm")
            add("Logging" to "logging")
            if ("slack" in optionalTabs) add("Slack" to "slack")
          }.sortedBy { it.first }.forEach { (label, tab) ->
            tabLink(label, tab, activeTab, config)
          }
        }
        if (config.docsUrl != null || config.apiDocsUrl != null || config.kdocsUrl != null) {
          hr {
            classes = setOf("border-gray-700", "my-4")
          }
          div {
            classes = setOf("space-y-1")
            listOfNotNull(
              config.docsUrl?.let { "Docs" to it },
              config.apiDocsUrl?.let { "Kairo Docs" to it },
              config.kdocsUrl?.let { "KDocs" to it },
            ).sortedBy { it.first }.forEach { (label, url) ->
              externalLink(label, url)
            }
          }
        }
      }
      main {
        classes = setOf("flex-1", "p-8", "overflow-auto", "bg-gray-50")
        content()
      }
    }
  }
}

private fun FlowContent.tabLink(
  label: String,
  tab: String,
  activeTab: String,
  config: AdminDashboardConfig,
) {
  val href = if (tab.isEmpty()) "${config.pathPrefix}/" else "${config.pathPrefix}/$tab"
  val isActive = tab == activeTab
  a(href = href) {
    classes = if (isActive) {
      setOf("block", "rounded-md", "p-2", "text-sm", "leading-6", "font-semibold", "bg-gray-800", "text-white")
    } else {
      setOf(
        "block",
        "rounded-md",
        "p-2",
        "text-sm",
        "leading-6",
        "font-semibold",
        "text-gray-400",
        "hover:text-white",
        "hover:bg-gray-800",
      )
    }
    +label
  }
}

private fun FlowContent.externalLink(label: String, url: String) {
  a(href = url) {
    attributes["target"] = "_blank"
    attributes["rel"] = "noopener noreferrer"
    classes = setOf(
      "flex",
      "items-center",
      "justify-between",
      "rounded-md",
      "p-2",
      "text-sm",
      "leading-6",
      "font-semibold",
      "text-gray-400",
      "hover:text-white",
      "hover:bg-gray-800",
    )
    +label
    unsafe { +externalLinkIcon }
  }
}

@Suppress("MaximumLineLength")
private val externalLinkIcon: String =
  """<svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M13.5 6H5.25A2.25 2.25 0 003 8.25v10.5A2.25 2.25 0 005.25 21h10.5A2.25 2.25 0 0018 18.75V10.5m-10.5 6L21 3m0 0h-5.25M21 3v5.25"/></svg>"""

@Suppress("MaximumLineLength")
private val hamburgerIcon: String =
  """<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"/></svg>"""

@Suppress("MaximumLineLength")
private val closeIcon: String =
  """<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/></svg>"""
