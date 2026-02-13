package kairo.admin.handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.Base64
import kairo.admin.AdminDashboardConfig
import kairo.admin.collector.ConfigCollector
import kairo.admin.collector.DatabaseCollector
import kairo.admin.collector.DependencyCollector
import kairo.admin.collector.EndpointCollector
import kairo.admin.collector.ErrorCollector
import kairo.admin.collector.HealthCheckCollector
import kairo.admin.collector.JvmCollector
import kairo.admin.collector.LoggingCollector
import kairo.admin.collector.PoolCollector
import kairo.admin.model.AdminIntegrationInfo
import kairo.admin.model.DashboardStats
import kairo.admin.model.SavedResponse
import kairo.admin.model.SqlQueryResult
import kairo.admin.view.adminLayout
import kairo.admin.view.authView
import kairo.admin.view.configView
import kairo.admin.view.databaseView
import kairo.admin.view.dependenciesView
import kairo.admin.view.emailView
import kairo.admin.view.endpointsView
import kairo.admin.view.errorsView
import kairo.admin.view.featuresView
import kairo.admin.view.healthView
import kairo.admin.view.homeView
import kairo.admin.view.integrationsView
import kairo.admin.view.jvmStatsPartial
import kairo.admin.view.jvmView
import kairo.admin.view.loggingView
import kairo.admin.view.slackView
import kairo.rest.HasRouting

@Suppress("LongMethod", "LongParameterList")
internal class AdminDashboardHandler(
  private val config: AdminDashboardConfig,
  private val endpointCollector: EndpointCollector,
  private val configCollector: ConfigCollector,
  private val jvmCollector: JvmCollector,
  private val databaseCollector: DatabaseCollector,
  private val poolCollector: PoolCollector,
  private val featureNames: List<String>,
  private val healthCheckCollector: HealthCheckCollector,
  private val loggingCollector: LoggingCollector,
  private val integrations: List<AdminIntegrationInfo>,
  private val dependencyCollector: DependencyCollector,
  private val errorCollector: ErrorCollector,
  private val slackChannels: Map<String, String>? = null,
  private val stytchModules: List<String>? = null,
  private val emailTemplates: Map<String, String>? = null,
) : HasRouting {
  private val optionalTabs: Set<String> = buildSet {
    if (slackChannels != null) add("slack")
    if (stytchModules != null) add("auth")
    if (emailTemplates != null) add("email")
  }

  @Suppress("CognitiveComplexMethod", "SuspendFunSwallowedCancellation")
  override fun Application.routing() {
    routing {
      staticResources("${config.pathPrefix}/static", "static/admin")

      route(config.pathPrefix) {
        homeRoute()
        endpointRoutes()
        configRoutes()
        jvmRoutes()
        databaseRoutes()
        featureRoutes()
        healthRoutes()
        loggingRoutes()
        dependencyRoutes()
        integrationRoutes()
        errorRoutes()
        if (slackChannels != null) slackRoutes()
        if (stytchModules != null) authRoutes()
        if (emailTemplates != null) emailRoutes()
      }
    }
  }

  @Suppress("SuspendFunSwallowedCancellation")
  private fun Route.homeRoute() {
    get("/") {
      val stats = DashboardStats(
        endpointCount = endpointCollector.collect().size,
        configFileCount = configCollector.collect().size,
        tableCount = try {
          databaseCollector.listTables().size
        } catch (_: Exception) {
          0
        },
        jvmUptime = jvmCollector.collectUptime(),
        featureCount = featureNames.size,
        healthCheckCount = healthCheckCollector.runAll().size,
        integrationCount = integrations.size,
        dependencyCount = dependencyCollector.collect().size,
        errorCount = errorCollector.getAll().size,
        slackChannelCount = slackChannels?.size,
        stytchModuleCount = stytchModules?.size,
        emailTemplateCount = emailTemplates?.size,
      )
      call.respondHtml {
        adminLayout(config, optionalTabs, "") {
          homeView(config, stats)
        }
      }
    }
  }

  private fun Route.endpointRoutes() {
    get("/endpoints") {
      val endpoints = endpointCollector.collect()
      call.respondHtml {
        adminLayout(config, optionalTabs, "endpoints") {
          endpointsView(config, endpoints)
        }
      }
    }

    get("/endpoints/{index}") {
      val index = call.parameters["index"]?.toIntOrNull() ?: 0
      val endpoints = endpointCollector.collect()
      if (index in endpoints.indices) {
        val savedResponse = call.request.queryParameters["resp"]?.let { encoded ->
          try {
            val json = String(Base64.getDecoder().decode(encoded))
            val rbody = call.request.queryParameters["rbody"]?.let { b ->
              try {
                String(Base64.getDecoder().decode(b))
              } catch (_: Exception) {
                b
              }
            }.orEmpty()
            SavedResponse.fromJson(json, rbody)
          } catch (_: Exception) {
            null
          }
        }
        call.respondHtml {
          adminLayout(config, optionalTabs, "endpoints") {
            endpointsView(config, endpoints, index, savedResponse)
          }
        }
      } else {
        call.respondRedirect("${config.pathPrefix}/endpoints")
      }
    }
  }

  private fun Route.configRoutes() {
    get("/config") {
      val sources = configCollector.collect()
      val effectiveConfig = configCollector.effectiveConfig()
      call.respondHtml {
        adminLayout(config, optionalTabs, "config") {
          configView(config, sources, effectiveConfig)
        }
      }
    }
  }

  private fun Route.jvmRoutes() {
    get("/jvm") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "jvm") {
          jvmView(config, jvmCollector)
        }
      }
    }

    get("/jvm/refresh") {
      call.respondHtml(HttpStatusCode.OK) {
        jvmStatsPartial(jvmCollector)
      }
    }
  }

  @Suppress("SuspendFunSwallowedCancellation", "CognitiveComplexMethod", "CyclomaticComplexMethod")
  private fun Route.databaseRoutes() {
    get("/database") {
      val tables = try {
        databaseCollector.listTables()
      } catch (_: Exception) {
        emptyList()
      }
      val poolStats = try {
        poolCollector.collect()
      } catch (_: Exception) {
        null
      }
      val sql = call.request.queryParameters["sql"]?.let { encoded ->
        try {
          String(Base64.getDecoder().decode(encoded))
        } catch (_: Exception) {
          encoded
        }
      }
      val queryResult = call.request.queryParameters["result"]?.let { encoded ->
        try {
          SqlQueryResult.fromJson(String(Base64.getDecoder().decode(encoded)))
        } catch (_: Exception) {
          null
        }
      } ?: sql?.takeIf { it.isNotBlank() }?.let { databaseCollector.executeQuery(it) }
      call.respondHtml {
        adminLayout(config, optionalTabs, "database") {
          databaseView(config, tables, null, null, queryResult, sql.orEmpty(), poolStats)
        }
      }
    }

    get("/database/tables/{schema}/{name}") {
      val schema = call.parameters["schema"] ?: "public"
      val tableName = call.parameters["name"].orEmpty()
      val tables = try {
        databaseCollector.listTables()
      } catch (_: Exception) {
        emptyList()
      }
      val columns = try {
        databaseCollector.getTableColumns(schema, tableName)
      } catch (_: Exception) {
        emptyList()
      }
      val poolStats = try {
        poolCollector.collect()
      } catch (_: Exception) {
        null
      }
      call.respondHtml {
        adminLayout(config, optionalTabs, "database") {
          databaseView(config, tables, "$schema.$tableName", columns, poolStats = poolStats)
        }
      }
    }

    get("/database/query") {
      val sql = call.request.queryParameters["sql"]?.let { encoded ->
        try {
          String(Base64.getDecoder().decode(encoded))
        } catch (_: Exception) {
          encoded
        }
      }.orEmpty()
      val selectedTable = call.request.queryParameters["table"]
      val result = call.request.queryParameters["result"]?.let { encoded ->
        try {
          SqlQueryResult.fromJson(String(Base64.getDecoder().decode(encoded)))
        } catch (_: Exception) {
          null
        }
      } ?: if (sql.isNotBlank()) databaseCollector.executeQuery(sql) else null
      val tables = try {
        databaseCollector.listTables()
      } catch (_: Exception) {
        emptyList()
      }
      val columns = selectedTable?.let { table ->
        val parts = table.split(".")
        try {
          databaseCollector.getTableColumns(
            parts.getOrElse(0) { "public" },
            parts.getOrElse(1) { table },
          )
        } catch (_: Exception) {
          emptyList()
        }
      }
      val poolStats = try {
        poolCollector.collect()
      } catch (_: Exception) {
        null
      }
      call.respondHtml {
        adminLayout(config, optionalTabs, "database") {
          databaseView(config, tables, selectedTable, columns, result, sql, poolStats)
        }
      }
    }

    post("/database/query") {
      val params = call.receiveParameters()
      val sql = params["sql"].orEmpty()
      val selectedTable = params["table"]
      val result = databaseCollector.executeQuery(sql)
      val tables = try {
        databaseCollector.listTables()
      } catch (_: Exception) {
        emptyList()
      }
      val columns = selectedTable?.let { table ->
        val parts = table.split(".")
        try {
          databaseCollector.getTableColumns(
            parts.getOrElse(0) { "public" },
            parts.getOrElse(1) { table },
          )
        } catch (_: Exception) {
          emptyList()
        }
      }
      val poolStats = try {
        poolCollector.collect()
      } catch (_: Exception) {
        null
      }
      call.respondHtml {
        adminLayout(config, optionalTabs, "database") {
          databaseView(config, tables, selectedTable, columns, result, sql, poolStats)
        }
      }
    }
  }

  private fun Route.featureRoutes() {
    get("/features") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "features") {
          featuresView(featureNames)
        }
      }
    }
  }

  private fun Route.healthRoutes() {
    get("/health") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "health") {
          healthView(config, emptyList(), healthCheckCollector.hasChecks)
        }
      }
    }

    post("/health/run") {
      val results = healthCheckCollector.runAll()
      call.respondHtml {
        adminLayout(config, optionalTabs, "health") {
          healthView(config, results, true)
        }
      }
    }
  }

  private fun Route.loggingRoutes() {
    get("/logging") {
      val loggers = try {
        loggingCollector.listLoggers()
      } catch (_: Exception) {
        emptyList()
      }
      call.respondHtml {
        adminLayout(config, optionalTabs, "logging") {
          loggingView(config, loggers)
        }
      }
    }

    post("/logging/level") {
      val params = call.receiveParameters()
      val loggerName = params["logger"].orEmpty()
      val level = params["level"].orEmpty()
      try {
        loggingCollector.setLevel(loggerName, level)
      } catch (_: Exception) {
        // Best-effort level change.
      }
      call.respondRedirect("${config.pathPrefix}/logging")
    }
  }

  private fun Route.dependencyRoutes() {
    get("/dependencies") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "dependencies") {
          dependenciesView(dependencyCollector.collect(), config)
        }
      }
    }
  }

  private fun Route.integrationRoutes() {
    get("/integrations") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "integrations") {
          integrationsView(integrations)
        }
      }
    }
  }

  private fun Route.errorRoutes() {
    get("/errors") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "errors") {
          errorsView(config, errorCollector.getAll())
        }
      }
    }

    post("/errors/clear") {
      errorCollector.clear()
      call.respondRedirect("${config.pathPrefix}/errors")
    }
  }

  private fun Route.slackRoutes() {
    get("/slack") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "slack") {
          slackView(slackChannels!!)
        }
      }
    }
  }

  private fun Route.authRoutes() {
    get("/auth") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "auth") {
          authView(stytchModules!!)
        }
      }
    }
  }

  private fun Route.emailRoutes() {
    get("/email") {
      call.respondHtml {
        adminLayout(config, optionalTabs, "email") {
          emailView(emailTemplates!!)
        }
      }
    }
  }
}
