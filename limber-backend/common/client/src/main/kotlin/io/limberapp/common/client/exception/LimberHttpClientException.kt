package io.limberapp.common.client.exception

import io.limberapp.error.LimberError

class LimberHttpClientException(val error: LimberError) : Exception(error.toString())
