package kairo.googleCloudScheduler

import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.exception.MissingRequiredAuthHeader
import kairo.rest.util.headerSingleNullOrThrow

private const val headerName: String = "X-CloudScheduler"

public fun AuthProvider.googleCloudScheduler(): Auth.Result {
  auth.request.headerSingleNullOrThrow(headerName)
    ?: return Auth.Result.Exception(MissingRequiredAuthHeader(headerName))
  return Auth.Result.Success
}
