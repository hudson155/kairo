package io.limberapp.client.exception

class LimberHttpClientInternalException(
  override val cause: Exception,
) : Exception("An error occurred while using a Limber client.", cause)
