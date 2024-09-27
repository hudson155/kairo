package kairo.rest.reader

internal class RestEndpointParamException(val name: String, e: Exception) : Exception(e)
