package kairo.admin

public data class AdminDashboardConfig(
  val pathPrefix: String = "/_admin",
  val title: String = "Kairo Admin",
  val serverName: String? = null,
  val docsUrl: String? = null,
  val apiDocsUrl: String? = null,
  val githubRepoUrl: String? = null,
)
