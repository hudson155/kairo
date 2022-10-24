package limber.testing

import limber.client.feature.FeatureClient
import limber.client.organization.OrganizationClient
import limber.client.organizationAuth.OrganizationAuthClient
import limber.client.organizationHostname.OrganizationHostnameClient
import limber.config.ConfigImpl
import limber.config.ConfigLoader
import limber.feature.TestRestFeature
import limber.feature.organization.OrganizationFeature
import limber.feature.rest.RestImplementation
import limber.feature.sql.TestSqlFeature

private val config = ConfigLoader.load<ConfigImpl>("testing")

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
  internal val organizationClient: OrganizationClient =
    injector.getInstance(OrganizationClient::class.java)

  internal val authClient: OrganizationAuthClient =
    injector.getInstance(OrganizationAuthClient::class.java)

  internal val hostnameClient: OrganizationHostnameClient =
    injector.getInstance(OrganizationHostnameClient::class.java)

  internal val featureClient: FeatureClient =
    injector.getInstance(FeatureClient::class.java)
}
