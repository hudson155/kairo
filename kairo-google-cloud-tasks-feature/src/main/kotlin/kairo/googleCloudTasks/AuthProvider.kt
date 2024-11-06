package kairo.googleCloudTasks

import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.exception.MissingRequiredAuthHeader
import kairo.rest.util.headerSingleNullOrThrow

private const val headerName: String = "X-AppEngine-QueueName"

public fun AuthProvider.googleCloudTask(): Auth.Result {
  auth.request.headerSingleNullOrThrow(headerName)
    ?: return Auth.Result.Exception(MissingRequiredAuthHeader(headerName))
  return Auth.Result.Success
}
