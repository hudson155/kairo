package com.piperframework.exception

import com.piperframework.exception.exception.notFound.NotFoundException

class EndpointNotFound : NotFoundException("This endpoint does not exist. Check the path and method.")
