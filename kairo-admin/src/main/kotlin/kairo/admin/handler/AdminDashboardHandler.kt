package kairo.admin.handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kairo.admin.AdminDashboardConfig
import kairo.admin.collector.ConfigCollector
import kairo.admin.collector.DatabaseCollector
import kairo.admin.collector.EndpointCollector
import kairo.admin.collector.JvmCollector
import kairo.admin.model.DashboardStats
import kairo.admin.view.adminLayout
import kairo.admin.view.configView
import kairo.admin.view.databaseQueryView
import kairo.admin.view.databaseView
import kairo.admin.view.endpointDetailView
import kairo.admin.view.endpointsView
import kairo.admin.view.homeView
import kairo.admin.view.jvmStatsPartial
import kairo.admin.view.jvmView
import kairo.rest.HasRouting

@Suppress("LongMethod", "LongParameterList")
internal class AdminDashboardHandler(
  private val config: AdminDashboardConfig,
  private val endpointCollector: EndpointCollector,
  private val configCollector: ConfigCollector,
  private val jvmCollector: JvmCollector,
  private val databaseCollector: DatabaseCollector,
) : HasRouting {
  @Suppress("CognitiveComplexMethod", "SuspendFunSwallowedCancellation")
  override fun Application.routing() {
    routing {
      staticResources("${config.pathPrefix}/static", "static/admin")

      route(config.pathPrefix) {
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
          )
          call.respondHtml {
            adminLayout(config, "") {
              homeView(config, stats)
            }
          }
        }

        get("/endpoints") {
          val endpoints = endpointCollector.collect()
          call.respondHtml {
            adminLayout(config, "endpoints") {
              endpointsView(config, endpoints)
            }
          }
        }

        get("/endpoints/{index}") {
          val index = call.parameters["index"]?.toIntOrNull() ?: 0
          val endpoints = endpointCollector.collect()
          val endpoint = endpoints.getOrNull(index)
          if (endpoint != null) {
            call.respondHtml {
              adminLayout(config, "endpoints") {
                endpointDetailView(config, endpoint, index)
              }
            }
          } else {
            call.respondRedirect("${config.pathPrefix}/endpoints")
          }
        }

        get("/config") {
          val sources = configCollector.collect()
          call.respondHtml {
            adminLayout(config, "config") {
              configView(config, sources, sources.firstOrNull()?.name)
            }
          }
        }

        get("/config/{name}") {
          val name = call.parameters["name"].orEmpty()
          val sources = configCollector.collect()
          call.respondHtml {
            adminLayout(config, "config") {
              configView(config, sources, name)
            }
          }
        }

        get("/jvm") {
          call.respondHtml {
            adminLayout(config, "jvm") {
              jvmView(config, jvmCollector)
            }
          }
        }

        get("/jvm/refresh") {
          call.respondHtml(HttpStatusCode.OK) {
            jvmStatsPartial(jvmCollector)
          }
        }

        get("/database") {
          val tables = try {
            databaseCollector.listTables()
          } catch (_: Exception) {
            emptyList()
          }
          call.respondHtml {
            adminLayout(config, "database") {
              databaseView(config, tables, null, null)
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
          call.respondHtml {
            adminLayout(config, "database") {
              databaseView(config, tables, "$schema.$tableName", columns)
            }
          }
        }

        post("/database/query") {
          val params = call.receiveParameters()
          val sql = params["sql"].orEmpty()
          val result = databaseCollector.executeQuery(sql)
          val tables = try {
            databaseCollector.listTables()
          } catch (_: Exception) {
            emptyList()
          }
          call.respondHtml {
            adminLayout(config, "database") {
              databaseQueryView(config, tables, sql, result)
            }
          }
        }
      }
    }
  }
}
