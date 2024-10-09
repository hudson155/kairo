package kairo.rest.cors

public data class KairoCorsConfig(
  val hosts: List<Host>,
  val headers: List<String>,
) {
  public data class Host(
    val host: String,
    val scheme: String,
  )
}
