package kairo.kdocs

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import kairo.feature.Feature
import kairo.rest.HasRouting

/**
 * Serves Dokka-generated KDocs from the classpath as static resources.
 * If no KDocs resources are found on the classpath, the feature is a no-op.
 *
 * To populate the classpath with KDocs, apply the `kairo-service-dokka` convention plugin
 * to your application module and run `./gradlew dokkaGenerate :your-app:packageKdocs`.
 */
public class KdocsFeature(
  public val config: KdocsConfig = KdocsConfig(),
) : Feature(), HasRouting {
  override val name: String = "KDocs"

  /**
   * Whether KDocs resources are available on the classpath.
   */
  public val available: Boolean =
    Thread.currentThread().contextClassLoader
      .getResource("${config.resourcePath}/index.html") != null

  override fun Application.routing() {
    if (!available) return
    routing {
      staticResources(config.pathPrefix, config.resourcePath)
    }
  }
}
