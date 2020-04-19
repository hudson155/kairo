package com.piperframework.exception

import com.piperframework.exception.exception.notFound.NotFoundException

internal class EndpointNotFound : NotFoundException("This endpoint does not exist. Check the path and method.")
