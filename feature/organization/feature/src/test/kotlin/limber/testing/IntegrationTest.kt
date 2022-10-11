package limber.testing

import limber.client.FeatureClient
import limber.client.OrganizationClient
import limber.client.OrganizationHostnameClient
import limber.config.ConfigLoader
import limber.config.OrganizationFeatureTestConfig
import limber.feature.OrganizationFeature
import limber.feature.TestRestFeature
import limber.rest.RestImplementation
import limber.sql.TestSqlFeature

private val config = ConfigLoader.load<OrganizationFeatureTestConfig>("testing")

private const val PORT: Int = 8081

internal abstract class IntegrationTest : FeatureIntegrationTest(
  config = config,
  featureUnderTest = OrganizationFeature(
    rest = RestImplementation.Http(baseUrl = "http://localhost:$PORT"),
  ),
  supportingFeatures = setOf(
    TestRestFeature(port = PORT),
    TestSqlFeature(config.sql, schemaName = "organization"),
  ),
) {
  protected val organizationClient: OrganizationClient =
    injector.getInstance(OrganizationClient::class.java)

  protected val hostnameClient: OrganizationHostnameClient =
    injector.getInstance(OrganizationHostnameClient::class.java)

  protected val featureClient: FeatureClient =
    injector.getInstance(FeatureClient::class.java)
}
