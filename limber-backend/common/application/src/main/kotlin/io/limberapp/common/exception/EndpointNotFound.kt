package io.limberapp.common.exception

import io.limberapp.common.exception.exception.notFound.NotFoundException

internal class EndpointNotFound : NotFoundException("This endpoint does not exist. Check the path and method.")
