package io.limberapp.server.exception

import io.limberapp.exception.NotFoundException

internal class EndpointNotFound :
    NotFoundException("This endpoint does not exist. Check the path and method.")
