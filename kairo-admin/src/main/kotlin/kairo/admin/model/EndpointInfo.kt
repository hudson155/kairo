package kairo.admin.model

public data class EndpointInfo(
  val method: String,
  val path: String,
  val pathParams: List<ParamInfo>,
  val queryParams: List<ParamInfo>,
  val contentType: String?,
  val accept: String?,
  val requestBodyType: String?,
  val responseType: String,
  val endpointClassName: String,
)
