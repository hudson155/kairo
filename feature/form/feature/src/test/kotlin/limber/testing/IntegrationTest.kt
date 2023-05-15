package limber.testing

import limber.config.ConfigImpl
import limber.config.ConfigLoader
import limber.feature.TestRestFeature
import limber.feature.form.FormFeature
import limber.feature.rest.RestImplementation
import limber.feature.sql.TestSqlFeature

private val config = ConfigLoader.load<ConfigImpl>("testing")

private const val PORT: Int = 8081

internal abstract class IntegrationTest : FeatureIntegrationTest(
  config = config,
  featureUnderTest = FormFeature(
    rest = RestImplementation.Http(baseUrl = "http://localhost:$PORT"),
  ),
  supportingFeatures = setOf(
    TestRestFeature(port = PORT),
    TestSqlFeature(config.sql, schemaName = "form"),
  ),
)
