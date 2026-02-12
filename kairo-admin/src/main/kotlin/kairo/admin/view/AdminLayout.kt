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
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.unsafe

@Suppress("LongMethod")
internal fun HTML.adminLayout(
  config: AdminDashboardConfig,
  activeTab: String,
  content: FlowContent.() -> Unit,
) {
  head {
    meta(charset = "utf-8")
    meta(name = "viewport", content = "width=device-width, initial-scale=1")
    title { +"${config.title} - Admin" }
    link(rel = "stylesheet", href = "${config.pathPrefix}/static/css/tailwind.css")
    script(src = "${config.pathPrefix}/static/vendor/turbo.es2017-esm.js") {
      attributes["type"] = "module"
    }
    script(src = "${config.pathPrefix}/static/js/admin.js") {
      attributes["type"] = "module"
    }
  }
  body {
    classes = setOf("bg-gray-50", "min-h-screen")
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
        classes = setOf("text-white", "p-2")
        attributes["data-action"] = "click->sidebar#toggle"
        span { unsafe { +"&#9776;" } }
      }
      span {
        classes = setOf("text-white", "font-bold", "ml-auto", "pr-4")
        +config.title
      }
    }
    div {
      classes = setOf("flex", "min-h-screen")
      // Sidebar: hidden off-screen on mobile, always visible on md+.
      nav {
        classes = setOf(
          "w-64",
          "bg-gray-900",
          "text-white",
          "min-h-screen",
          "p-4",
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
          classes = setOf("block", "text-xl", "font-bold", "mb-8", "hover:text-gray-300")
          +config.title
        }
        tabLink("Home", "", activeTab, config)
        tabLink("Config", "config", activeTab, config)
        tabLink("Database", "database", activeTab, config)
        tabLink("Dependencies", "dependencies", activeTab, config)
        tabLink("Endpoints", "endpoints", activeTab, config)
        tabLink("Errors", "errors", activeTab, config)
        tabLink("Features", "features", activeTab, config)
        tabLink("Health", "health", activeTab, config)
        tabLink("Integrations", "integrations", activeTab, config)
        tabLink("JVM", "jvm", activeTab, config)
        tabLink("Logging", "logging", activeTab, config)
      }
      main {
        classes = setOf("flex-1", "p-6", "overflow-auto")
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
      setOf("block", "px-4", "py-2", "mb-1", "rounded-md", "bg-gray-700", "text-white", "font-medium")
    } else {
      setOf("block", "px-4", "py-2", "mb-1", "rounded-md", "text-gray-300", "hover:bg-gray-800", "hover:text-white")
    }
    +label
  }
}
