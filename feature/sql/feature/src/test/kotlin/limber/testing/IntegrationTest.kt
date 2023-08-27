package limber.testing

import limber.config.ConfigImpl
import limber.config.ConfigLoader
import limber.feature.sql.Sql
import limber.feature.sql.TestSqlFeature

private val config = ConfigLoader.load<ConfigImpl>("testing")

internal abstract class IntegrationTest : FeatureIntegrationTest(
  config = config,
  featureUnderTest = TestSqlFeature(config.sql, schemaName = "testing"),
) {
  protected val sql: Sql = injector.getInstance(Sql::class.java)
}
