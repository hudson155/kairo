package kairo.admin.model

public data class EndpointInfo(
  val method: String,
  val path: String,
  val pathParams: List<ParamInfo>,
  val queryParams: List<ParamInfo>,
  val contentType: String?,
  val accept: String?,
  val requestBodyType: String?,
  val requestBodyFields: List<ParamInfo>,
  val requestBodyExample: String?,
  val responseType: String,
  val responseFields: List<ParamInfo>,
  val endpointClassName: String,
  val qualifiedClassName: String,
  val inputType: String,
  val isDataObject: Boolean,
)
