package io.limberapp.common.client.exception

import io.limberapp.common.rep.LimberError

class LimberHttpClientException(val error: LimberError) : Exception(error.toString())
