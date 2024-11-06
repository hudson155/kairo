package kairo.googleCloudTasks

import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.exception.MissingRequiredAuthHeader
import kairo.rest.util.headerSingleNullOrThrow

private const val headerName: String = "X-AppEngine-QueueName"

internal fun AuthProvider.task(): Auth.Result {
  auth.request.headerSingleNullOrThrow(headerName)
    ?: return Auth.Result.Exception(MissingRequiredAuthHeader(headerName))
  return Auth.Result.Success
}
