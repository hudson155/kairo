package kairo.admin.collector

import kairo.admin.AdminConfigSource

public class ConfigCollector(
  private val configSources: List<AdminConfigSource>,
  private val effectiveConfig: String? = null,
) {
  public fun collect(): List<AdminConfigSource> = configSources

  public fun effectiveConfig(): String? = effectiveConfig
}
