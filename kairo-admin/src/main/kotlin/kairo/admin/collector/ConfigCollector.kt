package kairo.admin.collector

import kairo.admin.AdminConfigSource

public class ConfigCollector(
  private val configSources: List<AdminConfigSource>,
) {
  public fun collect(): List<AdminConfigSource> = configSources
}
